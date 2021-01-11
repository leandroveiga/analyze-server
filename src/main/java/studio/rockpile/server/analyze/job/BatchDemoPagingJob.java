package studio.rockpile.server.analyze.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.support.ClassifierCompositeItemProcessor;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.classify.Classifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;
import studio.rockpile.server.analyze.constant.PublicDomainDef;
import studio.rockpile.server.analyze.job.decider.StepNodeDecider;
import studio.rockpile.server.analyze.job.step.ItemChunkWriter;
import studio.rockpile.server.analyze.job.step.ItemFilterProcessor;
import studio.rockpile.server.analyze.job.step.ItemUpperCaseProcessor;
import studio.rockpile.server.analyze.util.SpringContextUtil;

import javax.sql.DataSource;
import java.util.*;

@Configuration
public class BatchDemoPagingJob implements StepExecutionListener {
    private static final Logger logger = LoggerFactory.getLogger(BatchDemoJob.class);
    private static final Integer FETCH_CHUNK_SIZE = 10;
    private Map<String, JobParameter> params;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    public BatchDemoPagingJob(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, DataSource dataSource) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.dataSource = dataSource;
    }

    @Bean
    public Job demoPagingJob() throws Exception {
        return jobBuilderFactory.get("demoPagingJob")
                .start(demoPagingItemStep())
                .build();
    }

    @Bean
    public Step demoPagingItemStep() throws Exception {
        long ts = Calendar.getInstance().getTimeInMillis();
        TaskletStep step = stepBuilderFactory.get("demo_paging_item_step")
                .listener(this)
                .<Map<String, Object>, Map<String, Object>>chunk(FETCH_CHUNK_SIZE) /*chunkSize=20表示读取完20个数据，再进行输出处理，泛型中指定了输入输出的类型*/
                .faultTolerant()
                .reader(accountPagingItemReader())
                .processor(demoClassifyItemProcessor())
                .writer(items -> {
                    for (Map<String, Object> item : items) {
                        logger.debug("item step writer: {}", item);
                    }
                })
                .build();
        return step;
    }

    @Bean
    public CompositeItemProcessor<Map<String, Object>, Map<String, Object>> demoCompositeItemProcessor() {
        CompositeItemProcessor<Map<String, Object>, Map<String, Object>> compositeProcessor = new CompositeItemProcessor<>();

        List<ItemProcessor<Map<String, Object>, Map<String, Object>>> processors = new ArrayList<>();
        processors.add(SpringContextUtil.getBean("itemUpperCaseProcessor", ItemUpperCaseProcessor.class));
        processors.add(SpringContextUtil.getBean("itemFilterProcessor", ItemFilterProcessor.class));
        compositeProcessor.setDelegates(processors);
        return compositeProcessor;
    }

    @Bean
    public ClassifierCompositeItemProcessor<Map<String, Object>, Map<String, Object>> demoClassifyItemProcessor() {
        ClassifierCompositeItemProcessor<Map<String, Object>, Map<String, Object>> classifyProcessor
                = new ClassifierCompositeItemProcessor<>();

        Map<String, ItemProcessor<Map<String, Object>, Map<String, Object>>> processors = new HashMap<>();
        processors.put("upperCase", SpringContextUtil.getBean("itemUpperCaseProcessor", ItemUpperCaseProcessor.class));
        processors.put("upperAndFilter", demoCompositeItemProcessor());

        Classifier<? super Map<String, Object>, ItemProcessor<?, ? extends Map<String, Object>>> classifier
                = (Classifier<? super Map<String, Object>, ItemProcessor<?, ? extends Map<String, Object>>>) classifiable -> {
            if ((Integer) classifiable.get("type") == 2) {
                logger.debug("item paging step processor(upperCase)");
                return processors.get("upperCase");
            } else {
                logger.debug("item paging step processor(upperAndFilter)");
                return processors.get("upperAndFilter");
            }
        };
        classifyProcessor.setClassifier(classifier);
        return classifyProcessor;
    }

    @Bean
    @StepScope
    public ItemReader<? extends Map<String, Object>> accountPagingItemReader() throws Exception {
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
            logger.debug("item paging step reader: {}", raw);
            return raw;
        };
        itemReader.setRowMapper(rowMapper);

        MySqlPagingQueryProvider provider = new MySqlPagingQueryProvider();
        provider.setSelectClause("id,name,type,balance,update_time");
        provider.setFromClause("from t_account");
        provider.setWhereClause("where name like 'rockpile%' and id >= 5030106");
        // provider.setGroupClause();
        // 指定查询数据的排序字段
        Map<String, Order> sort = new HashMap<>(1); /*设置HashMap初始大小=1，按id单字段排序*/
        sort.put("id", Order.ASCENDING);
        provider.setSortKeys(sort);
        itemReader.setQueryProvider(provider);
        itemReader.afterPropertiesSet();
        return itemReader;
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
