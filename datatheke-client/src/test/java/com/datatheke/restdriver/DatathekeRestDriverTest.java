package com.datatheke.restdriver;

import static org.fest.assertions.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;

import com.datatheke.restdriver.beans.Library;
import com.datatheke.restdriver.response.IdResponse;
import com.datatheke.restdriver.response.LibrariesResponse;
import com.datatheke.restdriver.response.LibraryResponse;

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
		// connect to datatheke
		DatathekeRestDriver driver = new DatathekeRestDriver();
		driver.authenticate(username, password);
		assertThat(driver.isConnected()).isTrue();

		LibraryResponse response = driver.getLibrary("unknown lib id");
		assertThat(response.isFound()).isFalse();
	}

	@Test
	public void should_create_update_and_delete_library() {
		// connect to datatheke
		DatathekeRestDriver driver = new DatathekeRestDriver();
		driver.authenticate(username, password);
		assertThat(driver.isConnected()).isTrue();

		// get number of libraries
		LibrariesResponse librariesResponse = driver.getLibraries();
		Integer nbLibrary = librariesResponse.getTotalItemCount();

		// create library
		Library library = new Library(null, "UnitTest library", "This library should be deleted in UnitTest !");
		IdResponse idResponse = driver.createLibrary(library);
		String libraryId = idResponse.getId();
		assertThat(libraryId).isNotEmpty();
		library.setId(libraryId);

		// check that library is well created
		LibraryResponse libraryResponse = driver.getLibrary(library.getId());
		assertThat(libraryResponse.getOrNull()).isEqualsToByComparingFields(library);

		// check number of libraries increased
		librariesResponse = driver.getLibraries();
		assertThat(librariesResponse.getTotalItemCount()).isEqualTo(nbLibrary + 1);

		// update library
		library.setName("UnitTest library 2");
		library.setDescription("This library should be deleted in UnitTest ! 2");
		driver.updateLibrary(library);

		// check that library is well updated
		libraryResponse = driver.getLibrary(library.getId());
		assertThat(libraryResponse.getOrNull()).isEqualsToByComparingFields(library);

		// delete library
		driver.deleteLibrary(library.getId());

		// check that number of libraries decreased
		librariesResponse = driver.getLibraries();
		assertThat(librariesResponse.getTotalItemCount()).isEqualTo(nbLibrary);
	}

}
