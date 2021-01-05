package studio.rockpile.server.analyze.constant;

public enum StepStatusEnum {
    INIT("init", "初始化"),
    RUNNING("running", "执行中"),
    SUCCESS("success", "成功"),
    ERROR("error", "失败");

    private final String key;
    private final String description;

    StepStatusEnum(final String key, final String description) {
        this.key = key;
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public String getDescription() {
        return description;
    }

    public static StepStatusEnum getType(String key) {
        StepStatusEnum[] values = StepStatusEnum.values();
        for (StepStatusEnum value : values) {
            if (value.getKey().equals(key)) {
                return value;
            }
        }
        return null;
    }
}
