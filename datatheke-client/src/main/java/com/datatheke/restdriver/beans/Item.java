package com.datatheke.restdriver.beans;

import java.util.Map;

public class Item {
	private String id;
	private Map<CollectionField, Object> values;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map<CollectionField, Object> getValues() {
		return values;
	}

	public void setValues(Map<CollectionField, Object> values) {
		this.values = values;
	}

}
