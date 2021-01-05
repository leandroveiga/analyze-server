package studio.rockpile.server.analyze.job;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.stereotype.Service;
import studio.rockpile.server.analyze.constant.DataTypeEnum;
import studio.rockpile.server.analyze.constant.PublicDomainDef;
import studio.rockpile.server.analyze.entity.JobNamedParam;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class JobParamConstructor {
    private static final Logger logger = LoggerFactory.getLogger(JobParamConstructor.class);
    private static final SimpleDateFormat formatter = new SimpleDateFormat(PublicDomainDef.DATE_FORMAT_DEFAULT_PATTERN);
    private static final SimpleDateFormat dayFormatter = new SimpleDateFormat("yyyyMMdd");

    public JobParameters build(String jobEnvBean, List<JobNamedParam> namedParams, Map<String, Object> args) throws Exception {
        // 生成job执行的命名参数实例
        Date launchTime = Calendar.getInstance().getTime();
        JobParametersBuilder builder = new JobParametersBuilder();
        // JobParameter : { Object parameter, ParameterType parameterType }
        Long day = Long.valueOf(dayFormatter.format(launchTime));
        builder.addParameter(PublicDomainDef.JOB_LAUNCH_TIME_PARAM_KEY, new JobParameter(launchTime));
        builder.addParameter(PublicDomainDef.JOB_LAUNCH_DAY_PARAM_KEY, new JobParameter(day));
        builder.addParameter(PublicDomainDef.JOB_ENV_BEAN_PARAM_KEY, new JobParameter(jobEnvBean));

        for (int i = 0; i < namedParams.size(); i++) {
            JobNamedParam namedParam = namedParams.get(i);
            String paramCode = namedParam.getParamCode();
            DataTypeEnum dataType = DataTypeEnum.getType(namedParam.getDataType());

            Object value = null;
            String defaultVal = namedParam.getDefaultValue();
            if (args.containsKey(paramCode)) {
                value = args.get(paramCode);
                Class<?> clazz = Class.forName(dataType.getClassName());
                if (!value.getClass().isAssignableFrom(clazz)) {
                    throw new Exception("命名参数（" + paramCode + "）不配置指定的参数类型");
                }
            }
            JobParameter parameter = instParameter(dataType, value, defaultVal);
            builder.addParameter(paramCode, parameter);
        }
        return builder.toJobParameters();
    }

    private JobParameter instParameter(DataTypeEnum dataType, Object value, String defaultVal) throws Exception {
        JobParameter parameter = null;
        switch (dataType) {
            case STRING:
                parameter = (value != null) ? new JobParameter(String.valueOf(value)) :
                        new JobParameter(String.valueOf(defaultVal));
                break;
            case INTEGER:
                parameter = (value != null) ? new JobParameter(((Integer) value).longValue()) :
                        new JobParameter(Long.valueOf(defaultVal));
                break;
            case LONG:
            case UTC_DATE:
                parameter = (value != null) ? new JobParameter((Long) value) :
                        new JobParameter(Long.valueOf(defaultVal));
                break;
            case DOUBLE:
                parameter = (value != null) ? new JobParameter((Double) value) :
                        new JobParameter(Double.valueOf(defaultVal));
                break;
            case DATE:
                parameter = (value != null) ? new JobParameter(new Date(((Date) value).getTime())) :
                        new JobParameter(formatter.parse(defaultVal));
                break;
        }
        return parameter;
    }
}
