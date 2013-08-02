package com.datatheke.restdriver.response;


public interface IBeanResponse<T> {
	public boolean isFound();

	public T getOrNull();
}
