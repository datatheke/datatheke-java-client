package com.datatheke.restdriver.beans;

public class CollectionField {
	private String label;
	private CollectionFieldType type;

	public CollectionField() {
	}

	public CollectionField(String label, CollectionFieldType type) {
		this.label = label;
		this.type = type;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public CollectionFieldType getType() {
		return type;
	}

	public void setType(CollectionFieldType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "CollectionField [label=" + label + ", type=" + type + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		CollectionField other = (CollectionField) obj;
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
