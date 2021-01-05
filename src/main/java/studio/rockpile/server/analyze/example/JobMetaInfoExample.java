package studio.rockpile.server.analyze.example;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import studio.rockpile.server.analyze.constant.DataTypeEnum;
import studio.rockpile.server.analyze.constant.SegmentNodeTypeEnum;
import studio.rockpile.server.analyze.constant.StepStatusEnum;
import studio.rockpile.server.analyze.entity.*;
import studio.rockpile.server.analyze.protocol.JobMetaInfo;
import studio.rockpile.server.analyze.protocol.StepMetaInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobMetaInfoExample {
    private static ObjectMapper jsonMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true) // 序列化时忽略transient属性
            .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL).configure(SerializationFeature.INDENT_OUTPUT, false)
            .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
            .registerModule(new SimpleModule().addSerializer(Long.class, ToStringSerializer.instance)
                    .addSerializer(Long.TYPE, ToStringSerializer.instance));

    private static Map<Long, StepLinkMeta> demoLinkHash = new HashMap<>();

    private static List<JobNamedParam> jobNamedParam() throws Exception {
        List<JobNamedParam> namedParams = new ArrayList<>();
        JobNamedParam regionParam = new JobNamedParam();
        regionParam.setId(2021010611L);
        regionParam.setJobId(2021010601L);
        regionParam.setParamName("区局编码");
        regionParam.setParamCode("region_code");
        regionParam.setRequire(false);
        regionParam.setDataType(DataTypeEnum.STRING.getAlias());
        regionParam.setDefaultValue("0591");
        namedParams.add(regionParam);

        JobNamedParam monthParam = new JobNamedParam();
        monthParam.setId(2021010612L);
        monthParam.setJobId(2021010601L);
        monthParam.setParamName("填报月份");
        monthParam.setParamCode("fill_month");
        monthParam.setRequire(true);
        monthParam.setDataType(DataTypeEnum.INTEGER.getAlias());
        namedParams.add(monthParam);

        JobNamedParam startIdParam = new JobNamedParam();
        startIdParam.setId(2021010613L);
        startIdParam.setJobId(2021010601L);
        startIdParam.setParamName("起始id");
        startIdParam.setParamCode("start_id");
        startIdParam.setRequire(false);
        startIdParam.setDataType(DataTypeEnum.LONG.getAlias());
        startIdParam.setDefaultValue("5030606");
        namedParams.add(startIdParam);

        JobNamedParam balanceParam = new JobNamedParam();
        balanceParam.setId(2021010614L);
        balanceParam.setJobId(2021010601L);
        balanceParam.setParamName("账户余额");
        balanceParam.setParamCode("balance");
        balanceParam.setRequire(false);
        balanceParam.setDataType(DataTypeEnum.DOUBLE.getAlias());
        balanceParam.setDefaultValue("100.00");
        namedParams.add(balanceParam);
        return namedParams;
    }

    private static StepMetaInfo chunkStep() throws Exception {
        StepMetaInfo chunkStep = new StepMetaInfo();
        StepMeta step = new StepMeta();
        step.setId(2021010621L);
        step.setStepTitle("从表单库获取填报信息");
        step.setStepCode("affair_form_fill_data");
        step.setJobId(2021010601L);
        step.setSegNodeType(SegmentNodeTypeEnum.DB_TABLE_SELECT_CHUNK.getKey());
        step.setRectangularX(100);
        step.setRectangularY(100);
        step.setDeciderId(null);
        step.setStepType(1); /*步骤类型：1起始 9结束 2接续*/
        chunkStep.setStep(step);
        chunkStep.setDecider(null);
        chunkStep.setStatus(StepStatusEnum.INIT);
        chunkStep.setPreviousLinks(null);

        StepLinkMeta link = new StepLinkMeta();
        link.setId(20210106211L);
        link.setStepFromId(2021010621L);
        link.setStepToId(2021010622L); // 计算器处理节点A
        link.setDeciderResult("*");
        demoLinkHash.put(20210106211L, link);

        List<Long> nextLinks = new ArrayList<>();
        nextLinks.add(20210106211L);
        chunkStep.setNextLinks(nextLinks);

        List<StepProperty> properties = new ArrayList<>();

        StepProperty columnProperty = new StepProperty();
        columnProperty.setId(10210106211L);
        columnProperty.setCode("SelectClause");
        columnProperty.setSeqNo(0);
        columnProperty.setStepId(2021010621L);
        columnProperty.setContent("id,name,type,balance,update_time");
        properties.add(columnProperty);

        StepProperty tableProperty = new StepProperty();
        tableProperty.setId(10210106210L);
        tableProperty.setCode("FromClause");
        tableProperty.setSeqNo(1);
        tableProperty.setStepId(2021010621L);
        tableProperty.setContent("t_account");
        properties.add(tableProperty);

        StepProperty conditionProperty = new StepProperty();
        conditionProperty.setId(10210106212L);
        conditionProperty.setCode("WhereClause");
        conditionProperty.setSeqNo(2);
        conditionProperty.setStepId(2021010621L);
        conditionProperty.setContent("name like 'rockpile%' and type=1 and id >= ? and balance >= ?");
        properties.add(conditionProperty);

        chunkStep.setProperties(properties);
        return chunkStep;
    }

    private static StepMetaInfo calculatorStepA() throws Exception {
        StepMetaInfo flowStep = new StepMetaInfo();
        StepMeta step = new StepMeta();
        step.setId(2021010622L);
        step.setStepTitle("计算器处理节点A");
        step.setStepCode("calculator");
        step.setJobId(2021010601L);
        step.setSegNodeType(SegmentNodeTypeEnum.CALCULATOR.getKey());
        step.setRectangularX(300);
        step.setRectangularY(100);
        step.setDeciderId(null);
        step.setStepType(2); /*步骤类型：1起始 9结束 2接续 3处理节点*/
        flowStep.setStep(step);
        flowStep.setDecider(null);
        flowStep.setStatus(StepStatusEnum.INIT);
        List<Long> previousLinks = new ArrayList<>();
        previousLinks.add(20210106211L);
        flowStep.setPreviousLinks(previousLinks);

        StepLinkMeta link = new StepLinkMeta();
        link.setId(20210106221L);
        link.setStepFromId(2021010622L);
        link.setStepToId(2021010629L); // 控制台输出节点
        link.setDeciderResult("*");
        demoLinkHash.put(20210106221L, link);

        List<Long> nextLinks = new ArrayList<>();
        nextLinks.add(20210106221L);
        flowStep.setNextLinks(nextLinks);

        List<StepProperty> properties = new ArrayList<>();
        flowStep.setProperties(properties);
        return flowStep;
    }

    private static StepMetaInfo consoleOut() throws Exception {
        StepMetaInfo consoleOutput = new StepMetaInfo();
        StepMeta step = new StepMeta();
        step.setId(2021010629L);
        step.setStepTitle("控制台输出节点");
        step.setStepCode("console-output");
        step.setJobId(2021010601L);
        step.setSegNodeType(SegmentNodeTypeEnum.CONSOLE_OUTPUT.getKey());
        step.setRectangularX(500);
        step.setRectangularY(100);
        step.setDeciderId(null);
        step.setStepType(9); /*步骤类型：1起始 9结束 2接续*/
        consoleOutput.setStep(step);
        consoleOutput.setDecider(null);
        consoleOutput.setStatus(StepStatusEnum.INIT);
        List<Long> previousLinks = new ArrayList<>();
        previousLinks.add(20210106221L);
        consoleOutput.setPreviousLinks(previousLinks);
        consoleOutput.setNextLinks(null);

        List<StepProperty> properties = new ArrayList<>();
        consoleOutput.setProperties(properties);
        return consoleOutput;
    }

    public static JobMetaInfo getMetaInfo() throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        JobMetaInfo jobMetaInfo = new JobMetaInfo();
        JobMeta job = new JobMeta();
        job.setId(2021010601L);
        job.setJobTitle("应用表单填报分析示例");
        job.setJobCode("AffairFormFillAnalyzeDemo");
        job.setPublishStatus(1); /*发布状态：0在建 1已发布*/
        job.setTenancyId(1L);
        job.setUpdateTime(formatter.parse("2021-01-01 08:30:00"));
        jobMetaInfo.setJob(job);

        jobMetaInfo.setNamedParams(jobNamedParam());

        List<StepMetaInfo> steps = new ArrayList<>();
        steps.add(chunkStep()); // 表数据分块查询
        steps.add(calculatorStepA()); // 计算器节点A
        steps.add(consoleOut()); // 控制台输出节点
        jobMetaInfo.setSteps(steps);

        jobMetaInfo.setLinkHash(demoLinkHash);
        String json = jsonMapper.writeValueAsString(jobMetaInfo);
        System.out.println("... json : " + json);
        return jobMetaInfo;
    }
}
