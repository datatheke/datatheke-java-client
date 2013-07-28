package com.datatheke.restsample;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.datatheke.restdriver.DatathekeRestDriver;
import com.datatheke.restdriver.response.GenericResponse;

public class Main {
	public static void main(String[] args) throws IOException {
		Properties prop = new Properties();
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream stream = loader.getResourceAsStream("auth.properties");
		prop.load(stream);
		String username = (String) prop.get("auth.username");
		String password = (String) prop.get("auth.password");

		DatathekeRestDriver driver = new DatathekeRestDriver();
		driver.setDebug(true);
		if (driver.authenticate(username, password).isConnected()) {
			GenericResponse librairies = driver.getLibrairies(0);
			System.out.println(librairies);
		} else {
			System.out.println("Couldn't establish connexion !!");
		}
	}
}
