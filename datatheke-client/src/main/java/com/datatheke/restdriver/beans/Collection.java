package com.datatheke.restdriver.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Collection {
	private String id;
	private String name;
	private String description;
	private List<Field> fields;

	// Boolean private
	// Boolean deleted
	// Date created_at
	// Date updated_at
	// List<?> readers

	public Collection() {
	}

	public Collection(String id, String name, String description, List<Field> fields) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.fields = fields;
	}

	public Collection(Map<String, Object> map) {
		this.id = (String) map.get("id");
		this.name = (String) map.get("name");
		this.description = (String) map.get("description");
		this.fields = new ArrayList<Field>();
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> fieldList = (List<Map<String, Object>>) map.get("fields");
		if (fieldList != null) {
			for (Map<String, Object> field : fieldList) {
				this.fields.add(new Field(field));
			}
		}
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

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

	public Field getField(String label) {
		if (fields != null) {
			for (Field field : fields) {
				if (field != null && field.getLabel() != null && field.getLabel().equals(label)) {
					return field;
				}
			}
		}
		return null;
	}

	public void addField(Field field) {
		if (fields == null) {
			fields = new ArrayList<Field>();
		}
		fields.add(field);
	}

	public void removeField(Field field) {
		if (fields == null) {
			fields = new ArrayList<Field>();
		}
		fields.remove(field);
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
