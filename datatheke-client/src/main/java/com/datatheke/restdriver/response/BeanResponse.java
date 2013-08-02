package com.datatheke.restdriver.response;

import com.sun.jersey.api.client.ClientResponse;

public abstract class BeanResponse<T> extends GenericResponse {
	public BeanResponse(ClientResponse response) {
		super(response);
	}

	public boolean isFound() {
		return get("id") != null;
	}

	public abstract T getOrNull();
}
