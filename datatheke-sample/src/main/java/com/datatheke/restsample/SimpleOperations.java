package com.datatheke.restsample;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datatheke.restdriver.DatathekeClient;
import com.datatheke.restdriver.bean.Collection;
import com.datatheke.restdriver.bean.Field;
import com.datatheke.restdriver.bean.FieldType;
import com.datatheke.restdriver.bean.Item;
import com.datatheke.restdriver.bean.Library;
import com.datatheke.restdriver.response.CollectionResponse;
import com.datatheke.restdriver.response.IdResponse;

/**
 * This class show you how to perform some simple and basic operations with this
 * datatheke-java-client
 */
public class SimpleOperations {
	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleOperations.class);

	public static void main(String[] args) {
		DatathekeClient driver = getLoggedToApi();
		String libraryId = createALibrary(driver);
		LOGGER.info("Create library with id {}", libraryId);
		String collectionId = createACollection(driver, libraryId);
		LOGGER.info("Create collection with id {}", collectionId);
		String itemId = createAnItem(driver, collectionId);
		LOGGER.info("Create item with id {}", itemId);

		// remove this line to see your objects created in your datatheke
		// workspace
		driver.deleteLibrary(libraryId);
		LOGGER.info("All created objects are removed !");
	}

	public static DatathekeClient getLoggedToApi() {
		try {
			Properties prop = new Properties();
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			InputStream stream = loader.getResourceAsStream("auth.properties");
			prop.load(stream);
			String username = (String) prop.get("auth.username");
			String password = (String) prop.get("auth.password");

			DatathekeClient driver = new DatathekeClient();
			driver.setDebug(true);
			return driver.authenticate(username, password);
		} catch (IOException e) {
			throw new IllegalStateException(
					"Unknown user : You must set your datatheke username/password to src/main/resources/auth-sample.properties and rename it to auth.properties",
					e);
		} catch (NullPointerException e) {
			throw new IllegalStateException(
					"Unknown user : You must set your datatheke username/password to src/main/resources/auth-sample.properties and rename it to auth.properties",
					e);
		}
	}

	public static String createALibrary(DatathekeClient driver) {
		IdResponse createLibrary = driver.createLibrary(new Library(null, "My first library", "A sample test library"));
		if (createLibrary.isStatusOk()) {
			return createLibrary.getId();
		}
		return null;
	}

	public static String createACollection(DatathekeClient driver, String libraryId) {
		// define fields for elements that you will store in that collection
		List<Field> fields = new ArrayList<Field>();
		fields.add(new Field(null, "key", FieldType.string));
		fields.add(new Field(null, "value", FieldType.string));
		Collection collection = new Collection(null, "My first collection", "A sample test collection", fields);
		IdResponse createCollection = driver.createCollection(libraryId, collection);
		if (createCollection.isStatusOk()) {
			return createCollection.getId();
		}
		return null;
	}

	public static String createAnItem(DatathekeClient driver, String collectionId) {
		// to create an item you must have it related collection with it fields
		// containing id
		CollectionResponse collectionResponse = driver.getCollection(collectionId);
		if (collectionResponse.isStatusOk()) {
			Collection collection = collectionResponse.getOrNull();
			// set item fields : they must match with collection fields
			Map<Field, Object> values = new HashMap<Field, Object>();
			values.put(collection.getField("key"), "key");
			values.put(collection.getField("value"), "value");
			Item item = new Item(null, values);
			IdResponse createItem = driver.createItem(collectionId, item);
			if (createItem.isStatusOk()) {
				return createItem.getId();
			}
		}
		return null;
	}
}
