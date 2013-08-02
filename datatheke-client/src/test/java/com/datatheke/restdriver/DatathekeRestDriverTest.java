package com.datatheke.restdriver;

import static org.fest.assertions.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;

import com.datatheke.restdriver.beans.Library;
import com.datatheke.restdriver.response.GenericResponse;

public class DatathekeRestDriverTest {
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
		DatathekeRestDriver driver = new DatathekeRestDriver();
		driver.authenticate(username, password);
		assertThat(driver.isConnected()).isTrue();
	}

	@Test
	public void should_not_found_library() {
		GenericResponse response = null;

		// connect to datatheke
		DatathekeRestDriver driver = new DatathekeRestDriver();
		driver.authenticate(username, password);
		assertThat(driver.isConnected()).isTrue();

		response = driver.getLibrary("unknown lib id");
		assertThat(response.get("id")).isNull();
	}

	@Test
	public void should_create_update_and_delete_library() {
		GenericResponse response = null;

		// connect to datatheke
		DatathekeRestDriver driver = new DatathekeRestDriver();
		driver.authenticate(username, password);
		assertThat(driver.isConnected()).isTrue();

		// get number of libraries
		response = driver.getLibraries();
		Integer nbLibrary = (Integer) response.get("total_item_count");

		// create library
		Library library = new Library(null, "UnitTest library", "This library should be deleted in UnitTest !");
		response = driver.createLibrary(library);
		String libraryId = (String) response.get("id");
		assertThat(libraryId).isNotEmpty();
		library.setId(libraryId);

		// check that library is well created
		response = driver.getLibrary(library.getId());
		assertThat(response.get("name")).isEqualTo("UnitTest library");
		assertThat(response.get("description")).isEqualTo("This library should be deleted in UnitTest !");

		// check number of libraries increased
		response = driver.getLibraries();
		assertThat((Integer) response.get("total_item_count")).isEqualTo(nbLibrary + 1);

		// update library
		library.setName("UnitTest library 2");
		library.setDescription("This library should be deleted in UnitTest ! 2");
		driver.updateLibrary(library);

		// check that library is well updated
		response = driver.getLibrary(library.getId());
		assertThat(response.get("name")).isEqualTo("UnitTest library 2");
		assertThat(response.get("description")).isEqualTo("This library should be deleted in UnitTest ! 2");

		// delete library
		response = driver.deleteLibrary(library.getId());

		// check that number of libraries decreased
		response = driver.getLibraries();
		assertThat((Integer) response.get("total_item_count")).isEqualTo(nbLibrary);
	}

}
