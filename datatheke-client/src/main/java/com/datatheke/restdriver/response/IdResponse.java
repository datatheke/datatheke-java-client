package com.datatheke.restdriver.response;

import com.sun.jersey.api.client.ClientResponse;

public class IdResponse extends GenericResponse {
	public IdResponse(ClientResponse response) {
		super(response);
	}

	public String getId() {
		return (String) get("id");
	}
}
