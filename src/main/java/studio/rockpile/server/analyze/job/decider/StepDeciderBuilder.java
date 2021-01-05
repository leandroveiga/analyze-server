package studio.rockpile.server.analyze.job.decider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;
import studio.rockpile.server.analyze.entity.DeciderMeta;

@Component
public class StepDeciderBuilder {
    private static final Logger logger = LoggerFactory.getLogger(StepDeciderBuilder.class);
    public static final String DECIDER_RES_CONTEXT_PARAM_NAME = "#decide_res";
    public static final String DECIDER_DEFAULT_RES = "*";

    public JobExecutionDecider build(DeciderMeta deciderMeta, String jobEnvBean) {
        JobExecutionDecider decider = (jobExecution, stepExecution) -> {
            ExecutionContext context = stepExecution.getExecutionContext();
            if (context.containsKey(DECIDER_RES_CONTEXT_PARAM_NAME)) {
                String result = context.getString(DECIDER_RES_CONTEXT_PARAM_NAME);
                logger.debug("decider result = {}", result);
                return new FlowExecutionStatus(result);
            } else {
                return new FlowExecutionStatus(DECIDER_DEFAULT_RES);
            }
        };
        return decider;
    }
}
