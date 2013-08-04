package com.datatheke.restdriver.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.datatheke.restdriver.bean.util.Pair;

public class Item {
	private String id;
	private Map<Field, Object> values;

	public Item(String id, Map<Field, Object> values) {
		this.id = id;
		this.values = values;
	}

	public Item(List<Field> fields, Map<String, Object> map) {
		this.id = (String) map.get("id");
		this.values = new HashMap<Field, Object>();
		if (fields != null) {
			for (Field field : fields) {
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

	public Map<Field, Object> getValues() {
		return values;
	}

	public void setValues(Map<Field, Object> values) {
		this.values = values;
	}

	public Field getField(String label) {
		if (values != null) {
			for (Entry<Field, Object> entry : values.entrySet()) {
				if (entry != null && entry.getKey() != null && entry.getKey().getLabel() != null && entry.getKey().getLabel().equals(label)) {
					return entry.getKey();
				}
			}
		}
		return null;
	}

	public Pair<Field, Object> getFieldData(String label) {
		Field field = getField(label);
		if (values != null && field != null) {
			return new Pair<Field, Object>(field, values.get(field));
		}
		return new Pair<Field, Object>();
	}

	public Object getFieldValue(Field field) {
		if (values != null) {
			return values.get(field);
		}
		return null;
	}

	public Object getFieldValue(String label) {
		return getFieldValue(getField(label));
	}

	public void setFieldValue(Field field, Object value) {
		if (values != null) {
			values.put(field, value);
		}
	}

	public void setFieldValue(String label, Object value) {
		setFieldValue(getField(label), value);
	}

	public void addField(Field field, Object value) {
		if (values == null) {
			values = new HashMap<Field, Object>();
		}
		values.put(field, value);
	}

	public void removeField(Field field) {
		if (values == null) {
			values = new HashMap<Field, Object>();
		}
		values.remove(field);
	}

	public void removeField(String label) {
		removeField(getField(label));
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
