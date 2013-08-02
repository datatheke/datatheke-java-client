package com.datatheke.restdriver.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.datatheke.restdriver.beans.Library;
import com.sun.jersey.api.client.ClientResponse;

public class LibrariesResponse extends ListResponse<Library> {
	public LibrariesResponse(ClientResponse response) {
		super(response);
	}

	@Override
	public List<Library> getItems() {
		List<Library> libraries = new ArrayList<Library>();
		@SuppressWarnings("unchecked")
		Map<String, Map<String, Object>> items = (Map<String, Map<String, Object>>) get("items");
		for (Map<String, Object> item : items.values()) {
			libraries.add(new Library(item));
		}
		return libraries;
	}
}
