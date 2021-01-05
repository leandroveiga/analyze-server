package studio.rockpile.server.analyze.job.step;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import studio.rockpile.server.analyze.config.DynamicBeanRegister;
import studio.rockpile.server.analyze.entity.DeciderMeta;
import studio.rockpile.server.analyze.job.BatchJobEnvironment;
import studio.rockpile.server.analyze.job.cache.FlowDataCacheHolder;
import studio.rockpile.server.analyze.job.decider.StepDeciderBuilder;
import studio.rockpile.server.analyze.protocol.StepMetaInfo;

import java.util.List;
import java.util.Map;

@Service
public class FlowStepConstructor {
    private static final Logger logger = LoggerFactory.getLogger(FlowStepConstructor.class);
    private final StepBuilderFactory stepBuilderFactory;

    @Autowired
    private StepDeciderBuilder deciderBuilder;

    public FlowStepConstructor(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }

    public Step build(StepMetaInfo stepMetaInfo, String jobEnvBean, Boolean isEnding) {
        String stepName = stepMetaInfo.getStep().getStepCode();
        StepBuilder builder = stepBuilderFactory.get(stepName);
        TaskletStep step = builder.tasklet((contribution, chunkContext) -> {
            BatchJobEnvironment jobEnv = DynamicBeanRegister.getBean(jobEnvBean, BatchJobEnvironment.class);
            FlowDataCacheHolder rawsCache = jobEnv.getRawsCache();
            String rawsIndexKey = "DefaultDataset";

            StepContext stepContext = chunkContext.getStepContext();
            String title = stepContext.getStepName() + "[" + Thread.currentThread().getName() + "]";

            List<Map<String, Object>> dataSet = rawsCache.getRawsHash().get(rawsIndexKey).getDataSet();

            // 模拟对数据的处理
            StringBuilder buff = new StringBuilder(title).append(" ids=");
            for (Map<String, Object> data : dataSet) {
                buff.append(data.get("id")).append(",");
            }
            logger.debug("{}", buff.toString());

            DeciderMeta deciderMeta = stepMetaInfo.getDecider();
            String decideResult = null;
            if (deciderMeta == null) {
                decideResult = StepDeciderBuilder.DECIDER_DEFAULT_RES;
            } else {
                String expression = deciderMeta.getExpression();
                // 解析expression表达式，获取决策结果
                // 按逻辑规则计算结果设置决策器，引入avaitor表达式引擎
                decideResult = StepDeciderBuilder.DECIDER_DEFAULT_RES;
            }
            ExecutionContext context = stepContext.getStepExecution().getExecutionContext();
            context.putString(StepDeciderBuilder.DECIDER_RES_CONTEXT_PARAM_NAME, decideResult);

            return RepeatStatus.FINISHED;
        }).build();

        if (!isEnding) {
            BatchJobEnvironment jobEnv = DynamicBeanRegister.getBean(jobEnvBean, BatchJobEnvironment.class);
            // 设置决策器校验Result
            DeciderMeta deciderMeta = stepMetaInfo.getDecider();
            JobExecutionDecider decider = deciderBuilder.build(deciderMeta, jobEnvBean);
            jobEnv.getDeciderSet().put(stepMetaInfo.getStep().getStepCode(), decider);
        }
        return step;
    }
}
