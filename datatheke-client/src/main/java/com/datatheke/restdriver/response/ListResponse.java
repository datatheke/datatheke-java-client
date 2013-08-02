package com.datatheke.restdriver.response;

import java.util.List;

import com.sun.jersey.api.client.ClientResponse;

public abstract class ListResponse<T> extends GenericResponse {
	public ListResponse(ClientResponse response) {
		super(response);
	}

	public Integer getPage() {
		return (Integer) get("page");
	}

	public Integer getPageCount() {
		return (Integer) get("page_count");
	}

	public Integer getItemPerPage() {
		return (Integer) get("per_page");
	}

	public Integer getTotalItemCount() {
		return (Integer) get("total_item_count");
	}

	public Integer getFirstItemNumber() {
		return (Integer) get("first_item_number");
	}

	public Integer getLastItemNumber() {
		return (Integer) get("last_item_number");
	}

	public Integer getCurrentItemCount() {
		return (Integer) get("current_item_count");
	}

	public abstract List<T> getItems();

}
