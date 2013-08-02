package com.datatheke.restdriver.response;

import com.sun.jersey.api.client.ClientResponse;

public class EmptyResponse extends GenericResponse {
	public EmptyResponse(ClientResponse response) {
		super(response);
	}
}
