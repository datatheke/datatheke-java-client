package com.datatheke.restdriver.beans;

public enum CollectionFieldType {
	string, textarea, date, coordinates;

	public static CollectionFieldType get(String name) {
		if (name != null) {
			for (CollectionFieldType type : CollectionFieldType.values()) {
				if (name.equals(type.toString())) {
					return type;
				}
			}
		}
		return null;
	}
}
