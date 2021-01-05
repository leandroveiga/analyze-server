package studio.rockpile.server.analyze.constant;

public class PublicDomainDef {
    public static final String RAWS_INDEX_KEY = "rawsKey";
    public static final String JOB_LAUNCH_TIME_PARAM_KEY = "launchTime";
    public static final String JOB_LAUNCH_DAY_PARAM_KEY = "launchDay";
    public static final String JOB_ENV_BEAN_PARAM_KEY = "jobEnvBean";
    public static final Integer STEP_TYPE_STARTING = 1;
    public static final Integer STEP_TYPE_RELAYING = 2;
    public static final Integer STEP_TYPE_HANDLER = 3;
    public static final Integer STEP_TYPE_ENDING = 9;
    public static final String DATE_FORMAT_DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";
}
