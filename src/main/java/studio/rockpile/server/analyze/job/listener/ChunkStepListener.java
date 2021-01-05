package studio.rockpile.server.analyze.job.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.AfterChunkError;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.stereotype.Component;
import studio.rockpile.server.analyze.config.DynamicBeanRegister;
import studio.rockpile.server.analyze.constant.PublicDomainDef;
import studio.rockpile.server.analyze.job.BatchJobEnvironment;

import java.util.Iterator;
import java.util.Map;

// 通过注解的方式实现chunk的监听
@Component
public class ChunkStepListener {
    private static final Logger logger = LoggerFactory.getLogger(ChunkStepListener.class);

    @BeforeChunk
    public void beforeChunk(ChunkContext context) {
        logger.debug("... Job({}) before chunk Step({})",
                context.getStepContext().getJobName(), context.getStepContext().getStepName());
        Map<String, JobParameter> params = context.getStepContext().getStepExecution().getJobParameters().getParameters();
        String jobEnvBean = params.get(PublicDomainDef.JOB_ENV_BEAN_PARAM_KEY).toString();
        BatchJobEnvironment jobEnv = DynamicBeanRegister.getBean(jobEnvBean, BatchJobEnvironment.class);
        if (jobEnv.getRawsCache().getParams() == null) {
            jobEnv.getRawsCache().setParams(params);
            // 输出Job运行时命名参数
            Iterator<Map.Entry<String, JobParameter>> iterator = params.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, JobParameter> next = iterator.next();
                logger.debug("job parameter [{}] : {}", next.getKey(), next.getValue().getValue());
            }
        }
    }

    @AfterChunk
    public void afterChunk(ChunkContext context) {
        logger.debug("... Job({}) after chunk Step({})",
                context.getStepContext().getJobName(), context.getStepContext().getStepName());
    }

    @AfterChunkError
    public void afterChunkError(ChunkContext context) {
        logger.debug("... Job({}) after error chunk Step({})",
                context.getStepContext().getJobName(), context.getStepContext().getStepName());
    }
}
