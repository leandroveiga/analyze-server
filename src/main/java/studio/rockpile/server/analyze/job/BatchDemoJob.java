package studio.rockpile.server.analyze.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;
import studio.rockpile.server.analyze.constant.PublicDomainDef;
import studio.rockpile.server.analyze.job.cache.FlowDataCacheHolder;
import studio.rockpile.server.analyze.job.decider.StepNodeDecider;
import studio.rockpile.server.analyze.job.listener.ChunkStepListener;
import studio.rockpile.server.analyze.job.listener.FlowStepListener;
import studio.rockpile.server.analyze.job.step.ItemChunkWriter;
import studio.rockpile.server.analyze.job.step.ItemFilterProcessor;
import studio.rockpile.server.analyze.util.SpringContextUtil;

import javax.sql.DataSource;
import java.util.*;

@Configuration
public class BatchDemoJob implements StepExecutionListener {
    private static final Logger logger = LoggerFactory.getLogger(BatchDemoJob.class);
    private static final String STEP_EXEC_ERR_CONTEXT_KEY = "#runtime_exception";
    private static final Integer FETCH_CHUNK_SIZE = 10;
    private Map<String, JobParameter> params;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    @Autowired
    private FlowDataCacheHolder rawsCache;
    @Autowired
    private ChunkStepListener chunkStepListener;
    @Autowired
    private FlowStepListener flowStepListener;

    public BatchDemoJob(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, DataSource dataSource) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.dataSource = dataSource;
    }

    @Bean
    public Job demoBatchJob() {
        long ts = Calendar.getInstance().getTimeInMillis();
        return jobBuilderFactory.get("demoBatchJob")
                .start(demoItemStep())
                .next(demoFlowStepA())
                .next(SpringContextUtil.getBean("stepNodeDecider", StepNodeDecider.class))
                .from(SpringContextUtil.getBean("stepNodeDecider", StepNodeDecider.class)).on("EVENT-B").to(demoFlowStepB())
                .from(SpringContextUtil.getBean("stepNodeDecider", StepNodeDecider.class)).on("EVENT-C").to(DemoFlowStepC())
                .from(demoFlowStepB()).next(demoFlowStepD())
                .end()
                .build();
    }

    @Bean
    public Step simpleItemStep() {
        TaskletStep step = stepBuilderFactory.get("simple_item_step")
                .listener(this)
                .<Map<String, Object>, Map<String, Object>>chunk(FETCH_CHUNK_SIZE)
                .faultTolerant() /*容错*/
                .reader(accountPagingItemRead())
                .processor((ItemProcessor<Map<String, Object>, Map<String, Object>>) item -> {
                    item.put("name", item.get("name").toString().toUpperCase());
                    logger.debug("item step processing: {}", item);
                    return item;
                })
                .writer(items -> {
                    for (Map<String, Object> item : items) {
                        logger.debug("item step writer: {}", item);
                    }
                })
                .build();
        return step;
    }

    @Bean
    public Step demoItemStep() {
        long ts = Calendar.getInstance().getTimeInMillis();
        TaskletStep step = stepBuilderFactory.get("demo_item_step")
                .listener(this)
                .<Map<String, Object>, Map<String, Object>>chunk(FETCH_CHUNK_SIZE) /*chunkSize=20表示读取完20个数据，再进行输出处理，泛型中指定了输入输出的类型*/
                .faultTolerant()
                .listener(chunkStepListener) /*chunk级别的监听*/
                .reader(accountPagingItemRead())
                // 一个ItemStep只能注册一个processor
                .processor(SpringContextUtil.getBean("itemFilterProcessor", ItemFilterProcessor.class))
                .writer(SpringContextUtil.getBean("itemChunkWriter", ItemChunkWriter.class))
                .build();
        return step;
    }

    @Bean
    @StepScope
    public ItemReader<? extends Map<String, Object>> accountPagingItemRead() {
        JdbcPagingItemReader<Map<String, Object>> itemReader = new JdbcPagingItemReader<>();
        itemReader.setDataSource(dataSource);
        itemReader.setFetchSize(FETCH_CHUNK_SIZE);
        RowMapper<Map<String, Object>> rowMapper = (rs, rowNum) -> {
            Map<String, Object> raw = new HashMap<>();
            raw.put(PublicDomainDef.RAWS_INDEX_KEY, "job.demo-" + params.get("job_inst_id").getValue());
            raw.put("id", rs.getLong("id"));
            raw.put("name", rs.getString("name"));
            raw.put("type", rs.getInt("type"));
            raw.put("balance", rs.getBigDecimal("balance"));
            raw.put("update_time", rs.getTimestamp("update_time"));
            return raw;
        };
        itemReader.setRowMapper(rowMapper);

        MySqlPagingQueryProvider provider = new MySqlPagingQueryProvider();
        provider.setSelectClause("id,name,type,balance,update_time");
        provider.setFromClause("from t_account");
        provider.setWhereClause("where name like 'rockpile%' and type=1");
        // provider.setGroupClause();
        // 指定查询数据的排序字段
        Map<String, Order> sort = new HashMap<>(1); /*设置HashMap初始大小=1，按id单字段排序*/
        sort.put("id", Order.ASCENDING);
        provider.setSortKeys(sort);
        itemReader.setQueryProvider(provider);

        return itemReader;
    }

    @Bean
    public Step demoFlowStepA() {
        StepBuilder builder = stepBuilderFactory.get("DemoFlowStep-A").listener(flowStepListener);
        TaskletStep step = builder.tasklet((contribution, chunkContext) -> {
            StepContext stepContext = chunkContext.getStepContext();
            String title = stepContext.getStepName() + " [" + Thread.currentThread().getName() + "]";
            try {
                List<Map<String, Object>> dataSet = rawsCache.getRawsHash()
                        .get("job.demo-" + stepContext.getJobInstanceId()).getDataSet();
                logger.debug("{}：raw[0].id=\"{}\"", title, dataSet.get(0).get("id"));

                // 设置决策器校验Result
                ExecutionContext context = stepContext.getStepExecution().getExecutionContext();
                context.putString(StepNodeDecider.DECIDER_RES_CONTEXT_PARAM_NAME, "EVENT-B");
            } catch (Exception e) {
                logger.error("{} failed: {}", title, e);
                rawsCache.setErrorStep(title);
            }
            return RepeatStatus.FINISHED;
        }).build();
        return step;
    }

    @Bean
    public Step demoFlowStepB() {
        StepBuilder builder = stepBuilderFactory.get("DemoFlowStep-B").listener(flowStepListener);
        TaskletStep step = builder.tasklet((contribution, chunkContext) -> {
            StepContext context = chunkContext.getStepContext();
            String title = context.getStepName() + " [" + Thread.currentThread().getName() + "]";
            try {
                List<Map<String, Object>> dataSet = rawsCache.getRawsHash()
                        .get("job.demo-" + context.getJobInstanceId()).getDataSet();
                logger.debug("{}：raw[0].id=\"{}\"", title, dataSet.get(0).get("id"));
            } catch (Exception e) {
                logger.error("{} failed: {}", title, e);
                rawsCache.setErrorStep(title);
            }
            return RepeatStatus.FINISHED;
        }).build();
        return step;
    }

    @Bean
    public Step DemoFlowStepC() {
        StepBuilder builder = stepBuilderFactory.get("DemoFlowStep-C").listener(flowStepListener);
        TaskletStep step = builder.tasklet((contribution, chunkContext) -> {
            StepContext context = chunkContext.getStepContext();
            String title = context.getStepName() + " [" + Thread.currentThread().getName() + "]";
            try {
                List<Map<String, Object>> dataSet = rawsCache.getRawsHash()
                        .get("job.demo-" + context.getJobInstanceId()).getDataSet();
                logger.debug("{}：raw[0].id=\"{}\"", title, dataSet.get(0).get("id"));
            } catch (Exception e) {
                logger.error("{} failed: {}", title, e);
                rawsCache.setErrorStep(title);
            }
            return RepeatStatus.FINISHED;
        }).build();
        return step;
    }

    @Bean
    public Step demoFlowStepD() {
        StepBuilder builder = stepBuilderFactory.get("DemoFlowStep-D").listener(flowStepListener);
        TaskletStep step = builder.tasklet((contribution, chunkContext) -> {
            StepContext context = chunkContext.getStepContext();
            String title = context.getStepName() + " [" + Thread.currentThread().getName() + "]";
            try {
                List<Map<String, Object>> dataSet = rawsCache.getRawsHash()
                        .get("job.demo-" + context.getJobInstanceId()).getDataSet();
                logger.debug("{}：raw[0].id=\"{}\"", title, dataSet.get(0).get("id"));
            } catch (Exception e) {
                logger.error("{} failed: {}", title, e.getMessage());
                rawsCache.setErrorStep(title);
            }
            return RepeatStatus.FINISHED;
        }).build();
        return step;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        this.params = stepExecution.getJobParameters().getParameters();
        this.params.put("job_inst_id", new JobParameter(stepExecution.getJobExecution().getJobInstance().getId()));
        // 输出Job运行时命名参数
        Iterator<Map.Entry<String, JobParameter>> iterator = this.params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, JobParameter> next = iterator.next();
            logger.debug("job parameter [{}] : {}", next.getKey(), next.getValue().getValue());
        }
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }

}
