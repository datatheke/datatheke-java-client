package com.datatheke.restdriver.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.datatheke.restdriver.beans.Collection;
import com.sun.jersey.api.client.ClientResponse;

public class CollectionsResponse extends ListResponse<Collection> {
	public CollectionsResponse(ClientResponse response) {
		super(response);
	}

	@Override
	public List<Collection> getItems() {
		List<Collection> collections = new ArrayList<Collection>();
		@SuppressWarnings("unchecked")
		Map<String, Map<String, Object>> items = (Map<String, Map<String, Object>>) get("items");
		for (Map<String, Object> item : items.values()) {
			collections.add(new Collection(item));
		}
		return collections;
	}
}
