package com.datatheke.restdriver;

import static org.fest.assertions.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.datatheke.restdriver.bean.Collection;
import com.datatheke.restdriver.bean.Field;
import com.datatheke.restdriver.bean.FieldType;
import com.datatheke.restdriver.bean.Item;
import com.datatheke.restdriver.bean.Library;
import com.datatheke.restdriver.response.CollectionResponse;
import com.datatheke.restdriver.response.CollectionsResponse;
import com.datatheke.restdriver.response.EmptyResponse;
import com.datatheke.restdriver.response.IdResponse;
import com.datatheke.restdriver.response.ItemResponse;
import com.datatheke.restdriver.response.ItemsResponse;
import com.datatheke.restdriver.response.LibrariesResponse;
import com.datatheke.restdriver.response.LibraryResponse;

public class DatathekeRestDriverTest {
	private static final String UNKNOWN_ID = "unknown id";
	private static final String NAME = "Default name for UnitTest !!!";
	private static final String DESCRIPTION = "Default description for UnitTest. This should be deleted at the end of test !!!";
	private static String username;
	private static String password;
	private String createdLibraryId;

	@BeforeClass
	public static void loadProperties() {
		Properties prop = new Properties();
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		// You must rename src/test/java/auth-sample.properties to
		// auth.properties and set your username and passord to test this driver
		InputStream stream = loader.getResourceAsStream("auth.properties");
		try {
			prop.load(stream);
			username = (String) prop.get("auth.username");
			password = (String) prop.get("auth.password");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Before
	public void clear() {
		createdLibraryId = null;
	}

	@After
	public void delete() {
		if (createdLibraryId != null) {
			DatathekeClient driver = new DatathekeClient();
			driver.authenticate(username, password);
			driver.deleteLibrary(createdLibraryId);
		}
	}

	@Test
	public void should_load_properties() {
		assertThat(username).isNotEmpty();
		assertThat(password).isNotEmpty();
	}

	@Test
	public void should_login_to_datatheke() {
		DatathekeClient driver = new DatathekeClient();
		driver.authenticate(username, password);
		assertThat(driver.isConnected()).isTrue();
	}

	@Test
	public void should_not_find_library() {
		// connect to datatheke
		DatathekeClient driver = new DatathekeClient();
		driver.authenticate(username, password);
		assertThat(driver.isConnected()).isTrue();

		LibraryResponse response = driver.getLibrary(UNKNOWN_ID);
		assertThat(response.isFound()).isFalse();
	}

	@Test
	public void should_create_update_and_delete_library() {
		// connect to datatheke
		DatathekeClient driver = new DatathekeClient();
		driver.authenticate(username, password);
		assertThat(driver.isConnected()).isTrue();

		// get number of libraries
		LibrariesResponse librariesResponse = driver.getLibraries();
		Integer nbLibrary = librariesResponse.getTotalItemCount();

		// create library
		Library library = new Library(null, NAME, DESCRIPTION);
		IdResponse idResponse = driver.createLibrary(library);
		createdLibraryId = idResponse.getId();
		assertThat(createdLibraryId).isNotEmpty();
		library.setId(createdLibraryId);

		// check that library is well created
		LibraryResponse libraryResponse = driver.getLibrary(library.getId());
		assertThat(libraryResponse.getOrNull()).isEqualsToByComparingFields(library);

		// check number of libraries increased
		librariesResponse = driver.getLibraries();
		assertThat(librariesResponse.getTotalItemCount()).isEqualTo(nbLibrary + 1);

		// update library
		library.setName(NAME + " 2");
		library.setDescription(DESCRIPTION + " 2");
		driver.updateLibrary(library);

		// check that library is well updated
		libraryResponse = driver.getLibrary(library.getId());
		assertThat(libraryResponse.getOrNull()).isEqualsToByComparingFields(library);

		// delete library
		driver.deleteLibrary(library.getId());
		createdLibraryId = null;

		// check that number of libraries decreased
		librariesResponse = driver.getLibraries();
		assertThat(librariesResponse.getTotalItemCount()).isEqualTo(nbLibrary);
	}

	@Test
	public void should_not_find_collection() {
		// connect to datatheke
		DatathekeClient driver = new DatathekeClient();
		driver.authenticate(username, password);
		assertThat(driver.isConnected()).isTrue();

		CollectionResponse response = driver.getCollection(UNKNOWN_ID);
		assertThat(response.isFound()).isFalse();
	}

	@Test
	public void should_create_update_and_delete_collection() {
		// connect to datatheke
		DatathekeClient driver = new DatathekeClient();
		driver.authenticate(username, password);
		assertThat(driver.isConnected()).isTrue();

		IdResponse createLibrary = driver.createLibrary(new Library(null, NAME, DESCRIPTION));
		createdLibraryId = createLibrary.getId();
		assertThat(createdLibraryId).isNotEmpty();

		CollectionsResponse collectionsResponse = driver.getCollectionsForLibrary(createdLibraryId);
		assertThat(collectionsResponse.getTotalItemCount()).isEqualTo(0);

		List<Field> fields = new ArrayList<Field>();
		fields.add(new Field(null, "key", FieldType.string));
		fields.add(new Field(null, "value", FieldType.string));
		IdResponse createCollection = driver.createCollection(createdLibraryId, new Collection(null, NAME, DESCRIPTION, fields));
		String collectionId = createCollection.getId();
		assertThat(collectionId).isNotEmpty();

		CollectionResponse collectionResponse = driver.getCollection(collectionId);
		Collection collection = collectionResponse.getOrNull();
		assertThat(collection).isNotNull();

		collectionsResponse = driver.getCollectionsForLibrary(createdLibraryId);
		assertThat(collectionsResponse.getTotalItemCount()).isEqualTo(1);

		collection.setName(NAME + " 2");
		collection.setDescription(DESCRIPTION + " 2");
		EmptyResponse updateCollection = driver.updateCollection(collection);
		assertThat(updateCollection.isStatusOk()).isTrue();

		collectionResponse = driver.getCollection(collectionId);
		assertThat(collectionResponse.getOrNull()).isNotNull().isEqualsToByComparingFields(collection);

		driver.deleteCollection(collectionId);

		collectionsResponse = driver.getCollectionsForLibrary(createdLibraryId);
		assertThat(collectionsResponse.getTotalItemCount()).isEqualTo(0);

		driver.deleteLibrary(createdLibraryId);
		createdLibraryId = null;
	}

	@Test
	public void should_add_field_to_a_collection() {
		// TODO
	}

	@Test
	public void should_remove_field_to_a_collection() {
		// TODO
	}

	@Test
	public void should_not_find_item() {
		// connect to datatheke
		DatathekeClient driver = new DatathekeClient();
		driver.authenticate(username, password);
		assertThat(driver.isConnected()).isTrue();

		IdResponse createLibrary = driver.createLibrary(new Library(null, NAME, DESCRIPTION));
		createdLibraryId = createLibrary.getId();
		assertThat(createdLibraryId).isNotEmpty();

		List<Field> fields = new ArrayList<Field>();
		fields.add(new Field(null, "key", FieldType.string));
		fields.add(new Field(null, "value", FieldType.string));
		IdResponse createCollection = driver.createCollection(createdLibraryId, new Collection(null, NAME, DESCRIPTION, fields));
		String collectionId = createCollection.getId();
		assertThat(collectionId).isNotEmpty();

		CollectionResponse collectionResponse = driver.getCollection(collectionId);
		Collection collection = collectionResponse.getOrNull();

		ItemResponse itemResponse = driver.getItem(collection, UNKNOWN_ID);
		assertThat(itemResponse.isFound()).isFalse();

		driver.deleteLibrary(createdLibraryId);
		createdLibraryId = null;
	}

	@Test
	public void should_create_update_and_delete_item() {
		// connect to datatheke
		DatathekeClient driver = new DatathekeClient();
		driver.authenticate(username, password);
		assertThat(driver.isConnected()).isTrue();

		IdResponse createLibrary = driver.createLibrary(new Library(null, NAME, DESCRIPTION));
		createdLibraryId = createLibrary.getId();
		assertThat(createdLibraryId).isNotEmpty();

		List<Field> fields = new ArrayList<Field>();
		fields.add(new Field(null, "key", FieldType.string));
		fields.add(new Field(null, "value", FieldType.string));
		IdResponse createCollection = driver.createCollection(createdLibraryId, new Collection(null, NAME, DESCRIPTION, fields));
		String collectionId = createCollection.getId();
		assertThat(collectionId).isNotEmpty();

		CollectionResponse collectionResponse = driver.getCollection(collectionId);
		Collection collection = collectionResponse.getOrNull();

		Map<Field, Object> values = new HashMap<Field, Object>();
		values.put(collection.getField("key"), "key");
		values.put(collection.getField("value"), "value");
		Item item = new Item(null, values);
		IdResponse createItem = driver.createItem(collection.getId(), item);
		assertThat(createItem.isStatusOk()).isTrue();
		String itemId = createItem.getId();
		assertThat(itemId).isNotNull();
		item.setId(itemId);

		ItemsResponse itemsResponse = driver.getItemsForCollection(collection);
		assertThat(itemsResponse.isStatusOk()).isTrue();
		assertThat(itemsResponse.getItems()).isNotEmpty().hasSize(1).contains(item);

		item.setFieldValue("key", "key 2");
		item.setFieldValue("value", "value 2");
		EmptyResponse updateItem = driver.updateItem(collection.getId(), item);
		assertThat(updateItem.isStatusOk()).isTrue();

		ItemResponse itemResponse = driver.getItem(collection, itemId);
		assertThat(itemResponse.isStatusOk()).isTrue();
		assertThat(itemResponse.getOrNull()).isEqualsToByComparingFields(item);

		EmptyResponse deleteItem = driver.deleteItem(collection.getId(), itemId);
		assertThat(deleteItem.isStatusOk()).isTrue();

		itemsResponse = driver.getItemsForCollection(collection);
		assertThat(itemsResponse.isStatusOk()).isTrue();
		assertThat(itemsResponse.getItems()).isEmpty();

		driver.deleteLibrary(createdLibraryId);
		createdLibraryId = null;
	}

	@Test
	public void check_field_type_compatibility() {
		// TODO this probably doesn't work yet
	}

}
