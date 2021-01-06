package studio.rockpile.server.analyze.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.JobFlowBuilder;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import studio.rockpile.server.analyze.config.DynamicBeanRegister;
import studio.rockpile.server.analyze.constant.PublicDomainDef;
import studio.rockpile.server.analyze.constant.SegmentNodeTypeEnum;
import studio.rockpile.server.analyze.entity.StepLinkMeta;
import studio.rockpile.server.analyze.job.decider.StepDeciderBuilder;
import studio.rockpile.server.analyze.job.step.ChunkStepConstructor;
import studio.rockpile.server.analyze.job.step.FlowStepConstructor;
import studio.rockpile.server.analyze.protocol.JobMetaInfo;
import studio.rockpile.server.analyze.protocol.StepMetaInfo;

import javax.sql.DataSource;
import java.util.List;

@Service
public class BatchJobConstructor {
    private static final Logger logger = LoggerFactory.getLogger(BatchJobConstructor.class);

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Autowired
    private ChunkStepConstructor chunkStepConstructor;
    @Autowired
    private FlowStepConstructor flowStepConstructor;

    public BatchJobConstructor(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, DataSource dataSource) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    private void buildSteps(JobMetaInfo jobMetaInfo, String jobEnvBean) throws Exception {
        BatchJobEnvironment jobEnv = DynamicBeanRegister.getBean(jobEnvBean, BatchJobEnvironment.class);
        List<StepMetaInfo> stepInfos = jobMetaInfo.getSteps();
        for (StepMetaInfo stepInfo : stepInfos) {
            SegmentNodeTypeEnum nodeType = SegmentNodeTypeEnum.getType(stepInfo.getStep().getSegNodeType());
            switch (nodeType) {
                case DB_TABLE_SELECT_CHUNK:
                    Step chunkStep = chunkStepConstructor.build(stepInfo, jobEnvBean);
                    jobEnv.getStepSet().put(stepInfo.getStep().getStepCode(), chunkStep);
                    break;
                case BIZ_RELATION:
                case CALCULATOR:
                case VALUE_MAPPER:
                case COLUMN_SELECTOR:
                case COLUMN_SPLIT:
                case RECORD_FILTER:
                case CUBE_OUTPUT:
                case CONSOLE_OUTPUT:
                    boolean isEnding = stepInfo.getStep().getStepType() == PublicDomainDef.STEP_TYPE_ENDING;
                    Step flowStep = flowStepConstructor.build(stepInfo, jobEnvBean, isEnding);
                    jobEnv.getStepSet().put(stepInfo.getStep().getStepCode(), flowStep);
                    break;
            }
        }
    }

    public String build(String jobEnvBean, JobMetaInfo jobMetaInfo) throws Exception {
        // 创建job涉及的Step
        buildSteps(jobMetaInfo, jobEnvBean);
        // 创建job
        buildJob(jobMetaInfo, jobEnvBean);
        return jobEnvBean;
    }

    private StepMetaInfo indexJobStep(List<StepMetaInfo> stepInfos, Long stepId) {
        for (StepMetaInfo stepInfo : stepInfos) {
            if (stepInfo.getStep().getId().equals(stepId)) {
                return stepInfo;
            }
        }
        return null;
    }

    private void buildJob(JobMetaInfo jobMetaInfo, String jobEnvBean) {
        BatchJobEnvironment env = DynamicBeanRegister.getBean(jobEnvBean, BatchJobEnvironment.class);
        // 按调用顺序生成流程节点调用链
        List<StepMetaInfo> stepInfos = jobMetaInfo.getSteps();
        JobFlowBuilder flowBuilder = null;
        SimpleJobBuilder jobStartBuilder = null;
        for (StepMetaInfo stepInfo : stepInfos) {
            String stepName = stepInfo.getStep().getStepCode();
            if (PublicDomainDef.STEP_TYPE_STARTING == stepInfo.getStep().getStepType()) {
                jobStartBuilder = jobBuilderFactory.get(jobMetaInfo.getJob().getJobCode()).start(env.getStepSet().get(stepName));
            } else if (PublicDomainDef.STEP_TYPE_RELAYING == stepInfo.getStep().getStepType()) {
                flowBuilder = jobStartBuilder.next(env.getStepSet().get(stepName))
                        .next(env.getDeciderSet().get(stepName));
            } else if (PublicDomainDef.STEP_TYPE_HANDLER == stepInfo.getStep().getStepType()) {
                flowBuilder.from(env.getStepSet().get(stepName))
                        .next(env.getDeciderSet().get(stepName));
            } else if (PublicDomainDef.STEP_TYPE_ENDING == stepInfo.getStep().getStepType()) {
                // Ending节点
            }
            if (PublicDomainDef.STEP_TYPE_STARTING != stepInfo.getStep().getStepType()
                    && PublicDomainDef.STEP_TYPE_ENDING != stepInfo.getStep().getStepType()) {
                // stepMeta的决策器及后续步骤节点
                List<Long> nextLinks = stepInfo.getNextLinks();
                for (Long linkId : nextLinks) {
                    StepLinkMeta linkMeta = jobMetaInfo.getLinkHash().get(linkId);
                    StepMetaInfo toStep = indexJobStep(jobMetaInfo.getSteps(), linkMeta.getStepToId());
                    String condition = null;
                    if (stepInfo.getDecider() == null) {
                        condition = stepInfo.getStep().getStepCode();
                    } else {
                        condition = linkMeta.getDeciderResult();
                    }
                    flowBuilder.from(env.getDeciderSet().get(stepName)).on(condition)
                            .to(env.getStepSet().get(toStep.getStep().getStepCode()));
                }
            }
        }
        Job job = flowBuilder.end().build();
        env.setJob(job);
    }


}
