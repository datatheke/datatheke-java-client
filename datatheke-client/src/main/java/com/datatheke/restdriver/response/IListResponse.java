package com.datatheke.restdriver.response;

import java.util.List;

public interface IListResponse<T> {
	public Integer getPage();

	public Integer getPageCount();

	public Integer getItemPerPage();

	public Integer getTotalItemCount();

	public Integer getFirstItemNumber();

	public Integer getLastItemNumber();

	public Integer getCurrentItemCount();

	public List<T> getItems();
}
