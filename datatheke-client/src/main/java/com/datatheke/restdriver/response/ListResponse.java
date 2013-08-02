package com.datatheke.restdriver.response;

import java.util.List;

import com.sun.jersey.api.client.ClientResponse;

public abstract class ListResponse<T> extends GenericResponse implements IListResponse<T> {
	public ListResponse(ClientResponse response) {
		super(response);
	}

	@Override
	public Integer getPage() {
		return (Integer) get("page");
	}

	@Override
	public Integer getPageCount() {
		return (Integer) get("page_count");
	}

	@Override
	public Integer getItemPerPage() {
		return (Integer) get("per_page");
	}

	@Override
	public Integer getTotalItemCount() {
		return (Integer) get("total_item_count");
	}

	@Override
	public Integer getFirstItemNumber() {
		return (Integer) get("first_item_number");
	}

	@Override
	public Integer getLastItemNumber() {
		return (Integer) get("last_item_number");
	}

	@Override
	public Integer getCurrentItemCount() {
		return (Integer) get("current_item_count");
	}

	public abstract List<T> getItems();

}
