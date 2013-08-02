package com.datatheke.restdriver.beans;

import java.util.Date;

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

	public static FieldType get(Class<?> clazz) {
		if (clazz != null) {
			if (clazz.equals(String.class)) {
				return string;
			} else if (clazz.equals(Date.class)) {
				return date;
			}
		}
		return null;
	}
}
