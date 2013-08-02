package com.datatheke.restdriver.beans;

import java.util.ArrayList;
import java.util.List;

public class Collection {
	private String id;
	private String name;
	private String description;
	private List<CollectionField> fields;

	public Collection() {
	}

	public Collection(String id, String name, String description, List<CollectionField> fields) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.fields = fields;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<CollectionField> getFields() {
		return fields;
	}

	public void setFields(List<CollectionField> fields) {
		this.fields = fields;
	}

	public void addField(CollectionField field) {
		if (fields == null) {
			fields = new ArrayList<CollectionField>();
		}
		fields.add(field);
	}

	public CollectionField getField(String label) {
		if (fields != null) {
			for (CollectionField field : fields) {
				if (field != null && field.getLabel() != null && field.getLabel().equals(label)) {
					return field;
				}
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return "Collection [id=" + id + ", name=" + name + ", description=" + description + ", fields=" + fields + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Collection other = (Collection) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
