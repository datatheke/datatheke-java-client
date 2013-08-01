package com.datatheke.restdriver;

import static org.fest.assertions.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;

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

}
