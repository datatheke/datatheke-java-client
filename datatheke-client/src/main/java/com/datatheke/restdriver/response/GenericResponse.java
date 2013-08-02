package com.datatheke.restdriver.response;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;

public class GenericResponse {
	private ClientResponse response;
	private Map<String, Object> values;

	public GenericResponse() {
		values = new HashMap<String, Object>();
	}

	public GenericResponse(ClientResponse response) throws JsonParseException, JsonMappingException, IOException {
		this.response = response;
		try {
			if (response != null && response.hasEntity()) {
				String entity = response.getEntity(String.class);
				ObjectMapper mapper = new ObjectMapper();
				this.values = mapper.readValue(entity, new TypeReference<Map<String, Object>>() {
				});
			}
		} catch (UniformInterfaceException e) {
			// TODO how to avoid exception ?
		}
		if (this.values == null) {
			this.values = new HashMap<String, Object>();
		}
	}

	public ClientResponse getResponse() {
		return response;
	}

	public Object get(String key) {
		return values.get(key);
	}

	public Collection<Object> values() {
		return values.values();
	}

	public Set<String> keySet() {
		return values.keySet();
	}

	public Set<Entry<String, Object>> entrySet() {
		return values.entrySet();
	}

	public boolean containsKey(String key) {
		return values.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return values.containsValue(value);
	}

	public boolean isEmpty() {
		return values.isEmpty();
	}

	public int size() {
		return values.size();
	}

	@Override
	public String toString() {
		return "GenericResponse [" + values + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		GenericResponse other = (GenericResponse) obj;
		if (values == null) {
			if (other.values != null)
				return false;
		} else if (!values.equals(other.values))
			return false;
		return true;
	}
}
