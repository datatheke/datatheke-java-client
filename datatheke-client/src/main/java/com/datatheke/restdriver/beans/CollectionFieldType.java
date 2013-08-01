package com.datatheke.restdriver.beans;

public enum CollectionFieldType {
	STRING, TEXTAREA, DATE, COORDINATES;

	@Override
	public String toString() {
		return this.toString().toLowerCase();
	}
}
