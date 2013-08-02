package com.datatheke.restdriver;

import com.datatheke.restdriver.beans.Collection;
import com.datatheke.restdriver.beans.Item;
import com.datatheke.restdriver.beans.Library;
import com.datatheke.restdriver.response.CollectionResponse;
import com.datatheke.restdriver.response.CollectionsResponse;
import com.datatheke.restdriver.response.GenericResponse;
import com.datatheke.restdriver.response.ItemResponse;
import com.datatheke.restdriver.response.ItemsResponse;
import com.datatheke.restdriver.response.LibrariesResponse;
import com.datatheke.restdriver.response.LibraryResponse;

public class DatathekeRestDriverFluent {
	private DatathekeRestDriver driver;
	private GenericResponse lastResponse;

	public DatathekeRestDriverFluent() {
		driver = new DatathekeRestDriver();
	}

	public DatathekeRestDriverFluent(String baseUri) {
		driver = new DatathekeRestDriver(baseUri);
	}

	public Boolean isConnected() {
		return driver.isConnected();
	}

	public DatathekeRestDriverFluent authenticate(String username, String password) {
		driver.authenticate(username, password);
		return this;
	}

	public LibrariesResponse getLibraries(Integer page) {
		lastResponse = driver.getLibraries(page);
		return (LibrariesResponse) lastResponse;
	}

	public LibrariesResponse getLibraries() {
		lastResponse = driver.getLibraries();
		return (LibrariesResponse) lastResponse;
	}

	public LibraryResponse getLibrary(String id) {
		lastResponse = driver.getLibrary(id);
		return (LibraryResponse) lastResponse;
	}

	public DatathekeRestDriverFluent createLibrary(Library library) {
		lastResponse = driver.createLibrary(library);
		return this;
	}

	public DatathekeRestDriverFluent updateLibrary(Library library) {
		lastResponse = driver.updateLibrary(library);
		return this;
	}

	public DatathekeRestDriverFluent deleteLibrary(String id) {
		lastResponse = driver.deleteLibrary(id);
		return this;
	}

	public CollectionsResponse getCollectionsForLibrary(String id, Integer page) {
		lastResponse = driver.getCollectionsForLibrary(id, page);
		return (CollectionsResponse) lastResponse;
	}

	public CollectionsResponse getCollectionsForLibrary(String id) {
		lastResponse = driver.getCollectionsForLibrary(id);
		return (CollectionsResponse) lastResponse;
	}

	public CollectionResponse getCollection(String id) {
		lastResponse = driver.getCollection(id);
		return (CollectionResponse) lastResponse;
	}

	public DatathekeRestDriverFluent createCollection(String libraryId, Collection collection) {
		lastResponse = driver.createCollection(libraryId, collection);
		return this;
	}

	public DatathekeRestDriverFluent updateCollection(Collection collection) {
		lastResponse = driver.updateCollection(collection);
		return this;
	}

	public DatathekeRestDriverFluent deleteCollection(String id) {
		lastResponse = driver.deleteCollection(id);
		return this;
	}

	public ItemsResponse getItemsForCollection(Collection collection, Integer page) {
		lastResponse = driver.getItemsForCollection(collection, page);
		return (ItemsResponse) lastResponse;
	}

	public ItemsResponse getItemsForCollection(Collection collection) {
		lastResponse = driver.getItemsForCollection(collection);
		return (ItemsResponse) lastResponse;
	}

	public ItemResponse getItem(Collection collection, String id) {
		lastResponse = driver.getItem(collection, id);
		return (ItemResponse) lastResponse;
	}

	public DatathekeRestDriverFluent createItem(String collectionId, Item item) {
		lastResponse = driver.createItem(collectionId, item);
		return this;
	}

	public DatathekeRestDriverFluent updateItem(String collectionId, Item item) {
		lastResponse = driver.updateItem(collectionId, item);
		return this;
	}

	public DatathekeRestDriverFluent deleteItem(String collectionId, String id) {
		lastResponse = driver.deleteItem(collectionId, id);
		return this;
	}

	public DatathekeRestDriver getDriver() {
		return driver;
	}

	public GenericResponse getLastResponse() {
		return lastResponse;
	}

	@SuppressWarnings("unchecked")
	public <T extends GenericResponse> T getLastResponse(Class<T> clazz) {
		return (T) lastResponse;
	}

	public void setDebug(boolean debug) {
		driver.setDebug(debug);
	}
}
