package com.datatheke.restdriver.beans;

import java.util.Date;

import com.datatheke.restdriver.beans.type.LatLon;

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
			if (String.class.equals(clazz)) {
				return string;
			} else if (Date.class.equals(clazz)) {
				return date;
			} else if (LatLon.class.equals(clazz)) {
				return coordinates;
			}
		}
		return null;
	}

	public static Class<?> get(FieldType type) {
		if (type != null) {
			if (string.equals(type)) {
				return String.class;
			} else if (date.equals(type)) {
				return Date.class;
			} else if (coordinates.equals(type)) {
				return LatLon.class;
			}
		}
		return null;
	}
}
