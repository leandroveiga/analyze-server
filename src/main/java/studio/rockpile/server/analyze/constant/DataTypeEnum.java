package studio.rockpile.server.analyze.constant;

public enum DataTypeEnum {
	STRING(1, "string", "java.lang.String"), INTEGER(2, "int", "java.lang.Integer"),
	LONG(5, "long", "java.lang.Long"), DOUBLE(6, "double", "java.lang.Double"),
	DATE(3, "date", "java.util.Timestamp"),UTC_DATE(4, "utc_date", "java.util.Timestamp");

	private final Integer key;
	private final String alias;
	private final String className;

	DataTypeEnum(final Integer key, final String alias, final String className) {
		this.key = key;
		this.alias = alias;
		this.className = className;
	}


	public static DataTypeEnum getType(String alias) {
		DataTypeEnum[] types = DataTypeEnum.values();
		for (DataTypeEnum type : types) {
			if (type.getAlias().equals(alias)) {
				return type;
			}
		}
		return null;
	}

	public static DataTypeEnum getType(Integer key) {
		DataTypeEnum[] types = DataTypeEnum.values();
		for (DataTypeEnum type : types) {
			if (key == type.getKey()) {
				return type;
			}
		}
		return null;
	}

	public Integer getKey() {
		return key;
	}

	public String getAlias() {
		return alias;
	}

	public String getClassName() {
		return className;
	}

}
