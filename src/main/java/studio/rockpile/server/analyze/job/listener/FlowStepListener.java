package studio.rockpile.server.analyze.job.listener;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import studio.rockpile.server.analyze.config.DynamicBeanRegister;
import studio.rockpile.server.analyze.constant.PublicDomainDef;
import studio.rockpile.server.analyze.job.BatchJobEnvironment;
import studio.rockpile.server.analyze.job.cache.FlowDataCacheHolder;

import java.util.Iterator;
import java.util.Map;

// 监听器用来监听批处理作业的执行情况
// 创建监听可以通过实现接口或者使用注解（注解参见DemoChunkListener）
// JobExecutionListener(before, after)
// StepExecutionListener(before, after)
// ChunkListener(before, after, error)
// ItemReadListener, ItemProcessListener, ItemWriteListener(before, after, error)
@Component
public class FlowStepListener implements StepExecutionListener {
    private static final Logger logger = LoggerFactory.getLogger(FlowStepListener.class);

    @Override
    public void beforeStep(StepExecution stepExecution) {
        logger.debug("... before flow Step({})", stepExecution.getStepName());
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        logger.debug("... after flow Step({})", stepExecution.getStepName());
        String jobEnvBean = stepExecution.getJobParameters().getParameters()
                .get(PublicDomainDef.JOB_ENV_BEAN_PARAM_KEY).getValue().toString();
        BatchJobEnvironment jobEnv = DynamicBeanRegister.getBean(jobEnvBean, BatchJobEnvironment.class);

        String errorStep = jobEnv.getRawsCache().getErrorStep();
        if (StringUtils.isNotEmpty(errorStep)) {
            logger.debug("... exception in Step({})", errorStep);
            return ExitStatus.FAILED;
        }
        return ExitStatus.COMPLETED;
    }
}
