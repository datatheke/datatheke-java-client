package com.datatheke.restdriver.beans;

public enum FieldType {
	string, textarea, date, coordinates;

	public static FieldType get(String name) {
		if (name != null) {
			for (FieldType type : FieldType.values()) {
				if (name.equals(type.toString())) {
					return type;
				}
			}
		}
		return null;
	}
}
