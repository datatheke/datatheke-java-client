package com.datatheke.restsample;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datatheke.restdriver.DatathekeClient;
import com.datatheke.restdriver.DatathekeUtils;
import com.datatheke.restdriver.bean.Collection;
import com.datatheke.restdriver.bean.Item;
import com.datatheke.restdriver.bean.Library;
import com.datatheke.restdriver.bean.util.Pair;
import com.datatheke.restdriver.response.CollectionResponse;
import com.datatheke.restdriver.response.IdResponse;

/**
 * This class show you how to perform some simple and basic operations with this
 * datatheke-java-client using utility class : DatathekeUtils
 */
public class SimpleOperationsWithUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleOperationsWithUtils.class);

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
		// this method inspect MyObject class and create a collection with all
		// needed fields. Now you just have to create it to datatheke
		Collection generatedCollection = DatathekeUtils.generateCollectionFor(MyObject.class);
		IdResponse createCollection = driver.createCollection(libraryId, generatedCollection);
		if (createCollection.isStatusOk()) {
			return createCollection.getId();
		}
		return null;
	}

	public static String createACollection2(DatathekeClient driver, String libraryId) {
		// this method generate the collection for MyObject class and create it
		// directly to datatheke
		return DatathekeUtils.createCollection(driver, libraryId, MyObject.class);
	}

	public static String createAnItem(DatathekeClient driver, String collectionId) {
		// to create an item you must have it related collection with it fields
		// containing id
		CollectionResponse collectionResponse = driver.getCollection(collectionId);
		if (collectionResponse.isStatusOk()) {
			Collection collection = collectionResponse.getOrNull();
			// this method will create an item form the requested object
			// according to collection fields
			Item item = DatathekeUtils.toItem(collection.getFields(), new MyObject("key", "value"));
			IdResponse createItem = driver.createItem(collectionId, item);
			if (createItem.isStatusOk()) {
				return createItem.getId();
			}
		}
		return null;
	}

	public static String createAnItem2(DatathekeClient driver, String collectionId) {
		// this method does exactly the same as createAnItem and return the
		// collection (retrieved from datatheke api) and the id for the created
		// item
		Pair<Collection, String> createItem = DatathekeUtils.createItem(driver, collectionId, new MyObject("key", "value"));
		return createItem.getValue();
	}

	public static class MyObject {
		private String key;
		private String value;

		public MyObject() {
		}

		public MyObject(String key, String value) {
			super();
			this.key = key;
			this.value = value;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return "MyObject [key=" + key + ", value=" + value + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((key == null) ? 0 : key.hashCode());
			result = prime * result + ((value == null) ? 0 : value.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			MyObject other = (MyObject) obj;
			if (key == null) {
				if (other.key != null)
					return false;
			} else if (!key.equals(other.key))
				return false;
			if (value == null) {
				if (other.value != null)
					return false;
			} else if (!value.equals(other.value))
				return false;
			return true;
		}
	}
}
