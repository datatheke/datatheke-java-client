package com.datatheke.restdriver;

import java.io.IOException;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.datatheke.restdriver.response.AuthenticateToken;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

public class DatathekeRestDriver {
	private final WebResource resource;
	private AuthenticateToken token;

	public DatathekeRestDriver(String baseUri) {
		resource = Client.create().resource(baseUri);
	}

	public Boolean authenticate(String username, String password) {
		resource.addFilter(new HTTPBasicAuthFilter(username, password));
		WebResource endpoint = resource.path("token");
		Builder builder = endpoint.accept(MediaType.APPLICATION_JSON_TYPE);
		ClientResponse response = builder.post(ClientResponse.class);
		System.out.println(response);
		String entity = response.getEntity(String.class);
		if (response.getStatus() == 200) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				token = mapper.readValue(entity, AuthenticateToken.class);
				return true;
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public void getLibrairies() {
		WebResource endpoint = resource.path("libraries");
		Builder builder = endpoint.accept(MediaType.APPLICATION_JSON_TYPE);
		ClientResponse response = builder.get(ClientResponse.class);
		System.out.println(response);
		System.out.println(response.getEntity(String.class));
	}

	private ExecutionResult query(String statement, Map<String, Object> params) {
		String json = Util.toJson(Util.createPostData(statement, params));
		ClientResponse response = resource.accept(MediaType.APPLICATION_JSON_TYPE).type(MediaType.APPLICATION_JSON_TYPE)
				.header("X-Stream", "true").post(ClientResponse.class, json);
		String result = response.getEntity(String.class);
		response.close();
		int status = response.getStatus();
		System.out.printf("POST %s %nstatus code [%d] %nresult %s%n", json, status, result);
		return Util.toResult(status, result);
	}
}
