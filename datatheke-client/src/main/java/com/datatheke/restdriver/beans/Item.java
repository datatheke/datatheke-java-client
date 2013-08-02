package com.datatheke.restdriver.beans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Item {
	private String id;
	private Map<CollectionField, Object> values;

	public Item(List<CollectionField> fields, Map<String, Object> map) {
		this.id = (String) map.get("id");
		this.values = new HashMap<CollectionField, Object>();
		if (fields != null) {
			for (CollectionField field : fields) {
				this.values.put(field, map.get("_" + field.getId()));
			}
		}
	}

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

	public Object getField(String label) {
		if (values != null) {
			for (Entry<CollectionField, Object> entry : values.entrySet()) {
				if (entry.getKey().getLabel().equals(label)) {
					return entry.getValue();
				}
			}
		}
		return null;
	}

	public Object getField(CollectionFieldType type, String label) {
		if (values != null) {
			for (Entry<CollectionField, Object> entry : values.entrySet()) {
				if (entry.getKey().getType().equals(type) && entry.getKey().getLabel().equals(label)) {
					return entry.getValue();
				}
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return "Item [id=" + id + ", values=" + values + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((values == null) ? 0 : values.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (values == null) {
			if (other.values != null)
				return false;
		} else if (!values.equals(other.values))
			return false;
		return true;
	}

}
