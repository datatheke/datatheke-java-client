package com.datatheke.restdriver.response;

import com.datatheke.restdriver.beans.Library;
import com.sun.jersey.api.client.ClientResponse;

public class LibraryResponse extends BeanResponse<Library> {
	public LibraryResponse(ClientResponse response) {
		super(response);
	}

	@Override
	public Library getOrNull() {
		if (isFound()) {
			return new Library(getValues());
		}
		return null;
	}
}
