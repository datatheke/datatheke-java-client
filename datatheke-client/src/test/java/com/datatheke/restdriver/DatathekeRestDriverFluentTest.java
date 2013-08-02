package com.datatheke.restdriver;

import static org.fest.assertions.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;

import com.datatheke.restdriver.beans.Collection;
import com.datatheke.restdriver.beans.Field;
import com.datatheke.restdriver.beans.FieldType;
import com.datatheke.restdriver.beans.Library;
import com.datatheke.restdriver.response.IdResponse;

public class DatathekeRestDriverFluentTest {
	private static final String UNKNOWN_ID = "unknown id";
	private static final String NAME = "Default name for UnitTest !!!";
	private static final String DESCRIPTION = "Default description for UnitTest. This should be deleted at the end of test !!!";
	private static String username;
	private static String password;

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

	@Test
	public void should_load_properties() {
		assertThat(username).isNotEmpty();
		assertThat(password).isNotEmpty();
	}

	@Test
	public void should_login_to_datatheke() {
		DatathekeRestDriverFluent driver = new DatathekeRestDriverFluent();
		driver.authenticate(username, password);
		assertThat(driver.isConnected()).isTrue();
	}

	@Test
	public void should_not_find_library() {
		// connect to datatheke
		DatathekeRestDriverFluent driver = new DatathekeRestDriverFluent();
		driver.authenticate(username, password);
		assertThat(driver.isConnected()).isTrue();

		assertThat(driver.getLibrary(UNKNOWN_ID).isFound()).isFalse();
	}

	@Test
	public void should_create_update_and_delete_library() {
		// connect to datatheke
		DatathekeRestDriverFluent driver = new DatathekeRestDriverFluent();
		driver.authenticate(username, password);
		assertThat(driver.isConnected()).isTrue();

		// get number of libraries
		Integer nbLibrary = driver.getLibraries().getTotalItemCount();

		// create library
		Library library = new Library(null, NAME, DESCRIPTION);
		String libraryId = driver.createLibrary(library).getLastResponse(IdResponse.class).getId();
		assertThat(libraryId).isNotEmpty();
		library.setId(libraryId);

		// check that library is well created
		assertThat(driver.getLibrary(library.getId()).getOrNull()).isEqualsToByComparingFields(library);

		// check number of libraries increased
		assertThat(driver.getLibraries().getTotalItemCount()).isEqualTo(nbLibrary + 1);

		// update library
		library.setName(NAME + " 2");
		library.setDescription(DESCRIPTION + " 2");
		driver.updateLibrary(library);

		// check that library is well updated
		assertThat(driver.getLibrary(library.getId()).getOrNull()).isEqualsToByComparingFields(library);

		// delete library
		driver.deleteLibrary(library.getId());

		// check that number of libraries decreased
		assertThat(driver.getLibraries().getTotalItemCount()).isEqualTo(nbLibrary);
	}

	@Test
	public void should_not_find_collection() {
		// connect to datatheke
		DatathekeRestDriverFluent driver = new DatathekeRestDriverFluent();
		driver.authenticate(username, password);
		assertThat(driver.isConnected()).isTrue();

		assertThat(driver.getCollection(UNKNOWN_ID).isFound()).isFalse();
	}

	@Test
	public void should_create_update_and_delete_collection() {
		// connect to datatheke
		DatathekeRestDriverFluent driver = new DatathekeRestDriverFluent();
		driver.authenticate(username, password);
		assertThat(driver.isConnected()).isTrue();

		String libraryId = driver.createLibrary(new Library(null, NAME, DESCRIPTION)).getLastResponse(IdResponse.class).getId();
		assertThat(libraryId).isNotEmpty();

		assertThat(driver.getCollectionsForLibrary(libraryId).getTotalItemCount()).isEqualTo(0);

		List<Field> fields = new ArrayList<Field>();
		fields.add(new Field(null, "key", FieldType.string));
		fields.add(new Field(null, "value", FieldType.string));
		String collectionId = driver.createCollection(libraryId, new Collection(null, NAME, DESCRIPTION, fields))
				.getLastResponse(IdResponse.class).getId();
		assertThat(collectionId).isNotEmpty();

		Collection collection = driver.getCollection(collectionId).getOrNull();

		assertThat(driver.getCollectionsForLibrary(libraryId).getTotalItemCount()).isEqualTo(1);

		collection.setName(NAME + " 2");
		collection.setDescription(DESCRIPTION + " 2");
		collection.addField(new Field(null, "text", FieldType.string));
		collection.removeField(collection.getField("value"));
		// TODO perform update => doesn't work actually
		// EmptyResponse emptyResponse = driver.updateCollection(collection);

		driver.deleteCollection(collectionId);

		assertThat(driver.getCollectionsForLibrary(libraryId).getTotalItemCount()).isEqualTo(0);

		driver.deleteLibrary(libraryId);
	}

	@Test
	public void should_not_find_item() {
		// connect to datatheke
		DatathekeRestDriverFluent driver = new DatathekeRestDriverFluent();
		driver.authenticate(username, password);
		assertThat(driver.isConnected()).isTrue();

		String libraryId = driver.createLibrary(new Library(null, NAME, DESCRIPTION)).getLastResponse(IdResponse.class).getId();
		assertThat(libraryId).isNotEmpty();

		List<Field> fields = new ArrayList<Field>();
		fields.add(new Field(null, "key", FieldType.string));
		fields.add(new Field(null, "value", FieldType.string));
		String collectionId = driver.createCollection(libraryId, new Collection(null, NAME, DESCRIPTION, fields))
				.getLastResponse(IdResponse.class).getId();
		assertThat(collectionId).isNotEmpty();

		Collection collection = driver.getCollection(collectionId).getOrNull();

		assertThat(driver.getItem(collection, UNKNOWN_ID).isFound()).isFalse();

		driver.deleteLibrary(libraryId);
	}

	@Test
	public void should_create_update_and_delete_item() {
		// TODO can't delete item actually...
	}

	@Test
	public void check_field_type_compatibility() {
		// TODO this probably doesn't work yet
	}

}
