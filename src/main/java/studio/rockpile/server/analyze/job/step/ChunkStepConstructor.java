package studio.rockpile.server.analyze.job.step;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Service;
import studio.rockpile.server.analyze.config.DynamicBeanRegister;
import studio.rockpile.server.analyze.job.BatchJobEnvironment;
import studio.rockpile.server.analyze.job.listener.ChunkStepListener;
import studio.rockpile.server.analyze.protocol.StepMetaInfo;
import studio.rockpile.server.analyze.util.SpringContextUtil;

import java.util.Map;

@Service
public class ChunkStepConstructor {
    public static final Integer DEFAULT_FETCH_CHUNK_SIZE = 10;
    private final StepBuilderFactory stepBuilderFactory;

    private final ItemChunkReaderBuilder readerBuilder;
    private final ItemChunkProcessorBuilder processorBuilder;
    private final ItemChunkWriterBuilder writerBuilder;


    public ChunkStepConstructor(StepBuilderFactory stepBuilderFactory,
                                ItemChunkReaderBuilder readerBuilder,
                                ItemChunkProcessorBuilder processorBuilder,
                                ItemChunkWriterBuilder writerBuilder) {
        this.stepBuilderFactory = stepBuilderFactory;
        this.readerBuilder = readerBuilder;
        this.processorBuilder = processorBuilder;
        this.writerBuilder = writerBuilder;
    }

    public Step build(StepMetaInfo stepInfo, String jobEnvBean) throws Exception {
        BatchJobEnvironment jobEnv = DynamicBeanRegister.getBean(jobEnvBean, BatchJobEnvironment.class);

        String stepName = stepInfo.getStep().getStepCode();
        // ItemReader<Map<String, Object>> reader = readerBuilder.jdbcPagingReaderBuild(stepInfo, jobEnvBean);
        ItemReader<Map<String, Object>> reader = readerBuilder.jdbcCursorReaderBuild(stepInfo, jobEnvBean);
        jobEnv.getChunkReaderSet().put(stepName, reader);
        ItemProcessor<Map<String, Object>, Map<String, Object>> processor = processorBuilder.build(stepInfo, jobEnvBean);
        jobEnv.getChunkProcessorSet().put(stepName, processor);
        ItemWriter<Map<String, Object>> writer = writerBuilder.build(stepInfo, jobEnvBean);
        jobEnv.getChunkWriterSet().put(stepName, writer);

        TaskletStep step = stepBuilderFactory.get(stepName)
                .<Map<String, Object>, Map<String, Object>>chunk(DEFAULT_FETCH_CHUNK_SIZE)
                .faultTolerant() /*容错*/
                .listener(SpringContextUtil.getBean(ChunkStepListener.class)) /*chunk级别的监听*/
                .reader(jobEnv.getChunkReaderSet().get(stepName))
                .processor(jobEnv.getChunkProcessorSet().get(stepName))
                .writer(jobEnv.getChunkWriterSet().get(stepName))
                .build();
        return step;
    }


}
