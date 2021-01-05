package studio.rockpile.server.analyze.job.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.AfterChunkError;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.stereotype.Component;

// 通过注解的方式实现chunk的监听
@Component
public class ChunkStepListener {
    private static final Logger logger = LoggerFactory.getLogger(ChunkStepListener.class);

    @BeforeChunk
    public void beforeChunk(ChunkContext context) {
        logger.debug("... Job({}) before chunk Step({})",
                context.getStepContext().getJobName(), context.getStepContext().getStepName());
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
