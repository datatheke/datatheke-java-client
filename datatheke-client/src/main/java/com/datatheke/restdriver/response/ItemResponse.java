package com.datatheke.restdriver.response;

import java.util.List;

import com.datatheke.restdriver.bean.Field;
import com.datatheke.restdriver.bean.Item;
import com.sun.jersey.api.client.ClientResponse;

public class ItemResponse extends BeanResponse<Item> {
	private List<Field> fields;

	public ItemResponse(ClientResponse response, List<Field> fields) {
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
