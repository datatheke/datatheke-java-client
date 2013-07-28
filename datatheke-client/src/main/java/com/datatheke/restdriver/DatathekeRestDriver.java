package com.datatheke.restdriver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.datatheke.restdriver.response.AuthenticateToken;
import com.datatheke.restdriver.response.GenericResponse;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

public class DatathekeRestDriver {
	private final WebResource resource;
	private AuthenticateToken token;

	public DatathekeRestDriver(String baseUri) {
		resource = Client.create().resource(baseUri);
	}

	public Boolean isConnected() {
		return token != null;
	}

	public DatathekeRestDriver authenticate(String username, String password) {
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
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return this;
	}

	public GenericResponse getLibrairies(Integer page) {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("page", page != null ? page.toString() : null);
		return query("libraries", parameters);
	}

	public GenericResponse getLibrairies() {
		return getLibrairies(null);
	}

	private GenericResponse query(String path, Map<String, String> parameters) {
		if (token != null) {
			WebResource endpoint = resource.path(path);
			if (parameters != null) {
				for (Entry<String, String> entry : parameters.entrySet()) {
					if (entry.getValue() != null) {
						endpoint.queryParam(entry.getKey(), entry.getValue());
					}
				}
			}
			Builder builder = endpoint.accept(MediaType.APPLICATION_JSON_TYPE);
			ClientResponse response = builder.header("Authorization", "Bearer " + token.getToken()).get(ClientResponse.class);
			System.out.println(response);
			try {
				return new GenericResponse(response);
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (ClientHandlerException e) {
				e.printStackTrace();
			} catch (UniformInterfaceException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return new GenericResponse();
		} else {
			throw new IllegalStateException("You must be authenticated before perform this action !!!");
		}
	}
}
