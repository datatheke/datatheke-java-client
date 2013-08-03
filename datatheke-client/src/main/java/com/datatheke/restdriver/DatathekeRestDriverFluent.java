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

	public DatathekeRestDriverFluent(DatathekeRestDriver driver) {
		this.driver = driver;
	}

	/**
	 * Test if the driver is connected and logged to the datatheke API
	 * 
	 * @return
	 */
	public Boolean isConnected() {
		return driver.isConnected();
	}

	/**
	 * Login to the datatheke API with the provided username and password. Check
	 * if it works with isConnected() method
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public DatathekeRestDriverFluent authenticate(String username, String password) {
		driver.authenticate(username, password);
		return this;
	}

	/**
	 * Get a specific page in the list of your librairies. A page is 20
	 * elements.
	 * 
	 * @param page
	 * @return
	 */
	public LibrariesResponse getLibraries(Integer page) {
		lastResponse = driver.getLibraries(page);
		return (LibrariesResponse) lastResponse;
	}

	/**
	 * Get the first page of your librairies (20 elements max)
	 * 
	 * @return
	 */
	public LibrariesResponse getLibraries() {
		lastResponse = driver.getLibraries();
		return (LibrariesResponse) lastResponse;
	}

	/**
	 * Get a libraray with it id
	 * 
	 * @param libraryId
	 * @return
	 */
	public LibraryResponse getLibrary(String libraryId) {
		lastResponse = driver.getLibrary(libraryId);
		return (LibraryResponse) lastResponse;
	}

	/**
	 * Create a new library. Library object must have a null id !
	 * 
	 * @param library
	 * @return id of the created library
	 */
	public DatathekeRestDriverFluent createLibrary(Library library) {
		lastResponse = driver.createLibrary(library);
		return this;
	}

	/**
	 * Update a library. Library object must have it id field filled.
	 * 
	 * @param library
	 * @return
	 */
	public DatathekeRestDriverFluent updateLibrary(Library library) {
		lastResponse = driver.updateLibrary(library);
		return this;
	}

	/**
	 * Delete a library and all its content.
	 * 
	 * @param libraryId
	 * @return
	 */
	public DatathekeRestDriverFluent deleteLibrary(String libraryId) {
		lastResponse = driver.deleteLibrary(libraryId);
		return this;
	}

	/**
	 * Get a specific page of collections in the specified library. A page is 20
	 * elements.
	 * 
	 * @param libraryId
	 * @param page
	 * @return
	 */
	public CollectionsResponse getCollectionsForLibrary(String libraryId, Integer page) {
		lastResponse = driver.getCollectionsForLibrary(libraryId, page);
		return (CollectionsResponse) lastResponse;
	}

	/**
	 * Get the first page of collections in the specified library (20 elements
	 * max)
	 * 
	 * @param libraryId
	 * @return
	 */
	public CollectionsResponse getCollectionsForLibrary(String libraryId) {
		lastResponse = driver.getCollectionsForLibrary(libraryId);
		return (CollectionsResponse) lastResponse;
	}

	/**
	 * Get a collection with it id
	 * 
	 * @param collectionId
	 * @return
	 */
	public CollectionResponse getCollection(String collectionId) {
		lastResponse = driver.getCollection(collectionId);
		return (CollectionResponse) lastResponse;
	}

	/**
	 * Create a new collection in the specified library. Collection object must
	 * have a null id !
	 * 
	 * @param libraryId
	 * @param collection
	 * @return
	 */
	public DatathekeRestDriverFluent createCollection(String libraryId, Collection collection) {
		lastResponse = driver.createCollection(libraryId, collection);
		return this;
	}

	/**
	 * Update a collection. Collection object must have it id field filled.
	 * 
	 * @param collection
	 * @return
	 */
	public DatathekeRestDriverFluent updateCollection(Collection collection) {
		lastResponse = driver.updateCollection(collection);
		return this;
	}

	/**
	 * Delete a collection and all its content.
	 * 
	 * @param collectionId
	 * @return
	 */
	public DatathekeRestDriverFluent deleteCollection(String collectionId) {
		lastResponse = driver.deleteCollection(collectionId);
		return this;
	}

	/**
	 * Get a specific page of items in the specified collection. A page is 20
	 * elements.
	 * 
	 * @param collection
	 * @param page
	 * @return
	 */
	public ItemsResponse getItemsForCollection(Collection collection, Integer page) {
		lastResponse = driver.getItemsForCollection(collection, page);
		return (ItemsResponse) lastResponse;
	}

	/**
	 * Get the first page of items in the specified collection (20 elements max)
	 * 
	 * @param collection
	 * @return
	 */
	public ItemsResponse getItemsForCollection(Collection collection) {
		lastResponse = driver.getItemsForCollection(collection);
		return (ItemsResponse) lastResponse;
	}

	/**
	 * Get an item with it collection and it id
	 * 
	 * @param collectionId
	 * @param itemId
	 * @return
	 */
	public ItemResponse getItem(Collection collection, String itemId) {
		lastResponse = driver.getItem(collection, itemId);
		return (ItemResponse) lastResponse;
	}

	/**
	 * Create a new item in the specified collection. Item object must have a
	 * null id !
	 * 
	 * @param collectionId
	 * @param item
	 * @return
	 */
	public DatathekeRestDriverFluent createItem(String collectionId, Item item) {
		lastResponse = driver.createItem(collectionId, item);
		return this;
	}

	/**
	 * Update an item. Item object must have it id field filled.
	 * 
	 * @param item
	 * @return
	 */
	public DatathekeRestDriverFluent updateItem(String collectionId, Item item) {
		lastResponse = driver.updateItem(collectionId, item);
		return this;
	}

	/**
	 * Delete an item.
	 * 
	 * @param collectionId
	 * @param itemId
	 * @return
	 */
	public DatathekeRestDriverFluent deleteItem(String collectionId, String itemId) {
		lastResponse = driver.deleteItem(collectionId, itemId);
		return this;
	}

	/**
	 * Get the basic dataheke driver.
	 * 
	 * @return
	 */
	public DatathekeRestDriver getDriver() {
		return driver;
	}

	/**
	 * Get the response for the last executed request.
	 * 
	 * @return
	 */
	public GenericResponse getLastResponse() {
		return lastResponse;
	}

	/**
	 * Get a typed response for the last executed request.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends GenericResponse> T getLastResponse(Class<T> clazz) {
		return (T) lastResponse;
	}

	/**
	 * Set debug mode to true to see in logs all calls made to the datatheke API
	 * 
	 * @param debug
	 */
	public void setDebug(boolean debug) {
		driver.setDebug(debug);
	}
}
