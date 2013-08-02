package com.datatheke.restdriver.response;

import java.util.List;

import com.datatheke.restdriver.beans.CollectionField;
import com.datatheke.restdriver.beans.Item;
import com.sun.jersey.api.client.ClientResponse;

public class ItemResponse extends BeanResponse<Item> {
	private List<CollectionField> fields;

	public ItemResponse(ClientResponse response, List<CollectionField> fields) {
		super(response);
		this.fields = fields;
	}

	@Override
	public Item getOrNull() {
		if (isFound()) {
			return new Item(fields, getValues());
		}
		return null;
	}
}
