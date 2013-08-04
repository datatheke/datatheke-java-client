package com.datatheke.restdriver.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.datatheke.restdriver.bean.Field;
import com.datatheke.restdriver.bean.Item;
import com.sun.jersey.api.client.ClientResponse;

public class ItemsResponse extends ListResponse<Item> {
	private List<Field> fields;

	public ItemsResponse(ClientResponse response, List<Field> fields) {
		super(response);
		this.fields = fields;
	}

	@Override
	public List<Item> getItems() {
		List<Item> collections = new ArrayList<Item>();
		try {
			@SuppressWarnings("unchecked")
			Map<String, Map<String, Object>> items = (Map<String, Map<String, Object>>) get("items");
			for (Map<String, Object> item : items.values()) {
				collections.add(new Item(fields, item));
			}
		} catch (ClassCastException e) {
			// nothing
		}
		return collections;
	}
}
