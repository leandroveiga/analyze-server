package studio.rockpile.server.analyze.constant;

public class PublicDomainDef {
    public static final String RAWS_INDEX_KEY = "rawsKey";
    public static final String JOB_LAUNCH_TIME_PARAM_KEY = "#launch_time";
    public static final String JOB_LAUNCH_DAY_PARAM_KEY = "#launch_day";
    public static final String JOB_LAUNCH_MONTH_PARAM_KEY = "#launch_month";
    public static final String JOB_LAUNCH_YEAR_PARAM_KEY = "#launch_year";
    public static final Integer STEP_TYPE_STARTING = 1;
    public static final Integer STEP_TYPE_RELAYING = 2;
    public static final Integer STEP_TYPE_HANDLER = 3;
    public static final Integer STEP_TYPE_ENDING = 9;
    public static final String DATE_FORMAT_DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";
}
