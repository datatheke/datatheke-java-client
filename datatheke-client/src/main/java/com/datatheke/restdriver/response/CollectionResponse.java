package com.datatheke.restdriver.response;

import com.datatheke.restdriver.beans.Collection;
import com.sun.jersey.api.client.ClientResponse;

public class CollectionResponse extends BeanResponse<Collection> {
	public CollectionResponse(ClientResponse response) {
		super(response);
	}

	@Override
	public Collection getOrNull() {
		if (isFound()) {
			return new Collection(getValues());
		}
		return null;
	}

	public Boolean isPrivate() {
		return (Boolean) get("private");
	}

	public Boolean isDeleted() {
		return (Boolean) get("deleted");
	}

	public String getCreatedAt() {
		return (String) get("created_at");
	}

	public String isUpdatedAt() {
		return (String) get("updated_at");
	}

	public Object getReaders() {
		return get("readers");
	}
}
