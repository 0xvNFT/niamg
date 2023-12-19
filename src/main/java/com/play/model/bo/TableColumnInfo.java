package com.play.model.bo;

public class TableColumnInfo {
	private String name;
	private String type;
	private String desc;
	private String camelName;
	private String identity;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		camelName = toCamelName();
	}

	private String toCamelName() {
		int len = name.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c = name.charAt(i);
			if (c == '_') {
				if (++i < len) {
					sb.append(Character.toUpperCase(name.charAt(i)));
				}
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getJavaType() {
		if (type.startsWith("timestamp") || type.startsWith("Date") || type.startsWith("date")) {
			return "Date";
		}
		switch (type) {
		case "integer":
			return "Long";
		case "smallint":
			return "Integer";
		case "character varying":
			return "String";
		case "numeric":
			return "BigDecimal";
		default:
			return "String";
		}
	}

	public String getCamelName() {
		return camelName;
	}

	public void setCamelName(String camelName) {
		this.camelName = camelName;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

}
