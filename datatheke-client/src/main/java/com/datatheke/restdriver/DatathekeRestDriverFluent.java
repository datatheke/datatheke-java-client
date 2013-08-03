package com.datatheke.restdriver;

import com.datatheke.restdriver.bean.Collection;
import com.datatheke.restdriver.bean.Item;
import com.datatheke.restdriver.bean.Library;
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

	public LibraryResponse getLibrary(String libraryId) {
		lastResponse = driver.getLibrary(libraryId);
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

	public DatathekeRestDriverFluent deleteLibrary(String libraryId) {
		lastResponse = driver.deleteLibrary(libraryId);
		return this;
	}

	public CollectionsResponse getCollectionsForLibrary(String libraryId, Integer page) {
		lastResponse = driver.getCollectionsForLibrary(libraryId, page);
		return (CollectionsResponse) lastResponse;
	}

	public CollectionsResponse getCollectionsForLibrary(String libraryId) {
		lastResponse = driver.getCollectionsForLibrary(libraryId);
		return (CollectionsResponse) lastResponse;
	}

	public CollectionResponse getCollection(String collectionId) {
		lastResponse = driver.getCollection(collectionId);
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

	public DatathekeRestDriverFluent deleteCollection(String collectionId) {
		lastResponse = driver.deleteCollection(collectionId);
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

	public ItemResponse getItem(Collection collection, String itemId) {
		lastResponse = driver.getItem(collection, itemId);
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

	public DatathekeRestDriverFluent deleteItem(String collectionId, String itemId) {
		lastResponse = driver.deleteItem(collectionId, itemId);
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
