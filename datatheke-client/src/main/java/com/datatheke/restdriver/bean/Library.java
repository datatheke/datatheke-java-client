package com.datatheke.restdriver.bean;

import java.util.Map;

public class Library {
	private String id;
	private String name;
	private String description;

	// Boolean private
	// Boolean collaborative
	// Date created_at
	// Date updated_at

	public Library() {
	}

	public Library(String name, String description) {
		this.id = null;
		this.name = name;
		this.description = description;
	}

	public Library(String id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}

	public Library(Map<String, Object> map) {
		this.id = (String) map.get("id");
		this.name = (String) map.get("name");
		this.description = (String) map.get("description");
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

	@Override
	public String toString() {
		return "Library [id=" + id + ", name=" + name + ", description=" + description + "]";
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
		Library other = (Library) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
