package studio.rockpile.server.analyze.constant;

public enum SegmentNodeTypeEnum {
    DB_TABLE_SELECT_CHUNK("db-tab-selector", "表数据分块查询"),
    DATA_JOIN("data-join", "业务数据关联"),
    CALCULATOR("calculator", "计算器"),
    VALUE_MAPPER("val-mapper", "值映射"),
    COLUMN_SELECTOR("col-selector", "字段选择"),
    COLUMN_SPLIT("col-split", "字段拆分"),
    RECORD_FILTER("record-filter", "条件过滤"),
    CUBE_OUTPUT("cube-output", "Cube输出"),
    CONSOLE_OUTPUT("console-output", "控制台输出");

    private final String key;
    private final String description;

    SegmentNodeTypeEnum(final String key, final String description) {
        this.key = key;
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public String getDescription() {
        return description;
    }

    public static SegmentNodeTypeEnum getType(String key) {
        SegmentNodeTypeEnum[] values = SegmentNodeTypeEnum.values();
        for (SegmentNodeTypeEnum value : values) {
            if (value.getKey().equals(key)) {
                return value;
            }
        }
        return null;
    }
}
