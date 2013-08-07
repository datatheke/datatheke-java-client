package com.datatheke.restdriver.bean;

import java.util.Map;

public class Field {
	private String id;
	private String label;
	private FieldType type;

	// Boolean multiple

	public Field() {
	}

	public Field(String label, FieldType type) {
		this.id = null;
		this.label = label;
		this.type = type;
	}

	public Field(String id, String label, FieldType type) {
		this.id = id;
		this.label = label;
		this.type = type;
	}

	public Field(Map<String, Object> map) {
		this.id = (String) map.get("id");
		this.label = (String) map.get("label");
		this.type = FieldType.get((String) map.get("type"));
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public FieldType getType() {
		return type;
	}

	public void setType(FieldType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "CollectionField [id=" + id + ", label=" + label + ", type=" + type + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Field other = (Field) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
}
