package com.datatheke.restdriver.response;

import com.datatheke.restdriver.bean.Library;
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

	public Boolean isPrivate() {
		return (Boolean) get("private");
	}

	public Boolean isCollaborative() {
		return (Boolean) get("collaborative");
	}

	public String getCreatedAt() {
		return (String) get("created_at");
	}

	public String isUpdatedAt() {
		return (String) get("updated_at");
	}
}
