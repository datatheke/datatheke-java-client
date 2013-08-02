package com.datatheke.restdriver.response;

import com.sun.jersey.api.client.ClientResponse;

public abstract class BeanResponse<T> extends GenericResponse implements IBeanResponse<T> {
	public BeanResponse(ClientResponse response) {
		super(response);
	}

	@Override
	public boolean isFound() {
		return get("id") != null;
	}

	@Override
	public abstract T getOrNull();
}
