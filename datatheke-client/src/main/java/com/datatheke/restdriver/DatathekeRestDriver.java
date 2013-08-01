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
	private boolean debug;

	public DatathekeRestDriver() {
		resource = Client.create().resource("http://www.datatheke.com/api/v1");
		debug = false;
	}

	public DatathekeRestDriver(String baseUri) {
		resource = Client.create().resource(baseUri);
		debug = false;
	}

	public Boolean isConnected() {
		return token != null;
	}

	public DatathekeRestDriver authenticate(String username, String password) {
		resource.addFilter(new HTTPBasicAuthFilter(username, password));
		WebResource endpoint = resource.path("token");
		Builder builder = endpoint.accept(MediaType.APPLICATION_JSON_TYPE);
		ClientResponse response = builder.post(ClientResponse.class);
		if (debug) {
			System.out.println(response);
		}
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

	/**
	 * GET librairies?page={page}
	 * 
	 * @param page
	 * @return
	 */
	public GenericResponse getLibrairies(Integer page) {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("page", page != null ? page.toString() : null);
		return query("GET", "libraries", parameters, null);
	}

	/**
	 * GET librairies
	 * 
	 * @return
	 */
	public GenericResponse getLibrairies() {
		return getLibrairies(null);
	}

	/**
	 * GET library/{id}
	 * 
	 * @param id
	 * @return
	 */
	public GenericResponse getLibrary(String id) {
		return query("GET", "library/" + id, null, null);
	}

	/**
	 * GET library/{id}/collections?page={page}
	 * 
	 * @param id
	 * @param page
	 * @return
	 */
	public GenericResponse getLibraryCollections(String id, Integer page) {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("page", page != null ? page.toString() : null);
		return query("GET", "library/" + id + "/collections", parameters, null);
	}

	/**
	 * GET library/{id}/collections
	 * 
	 * @param id
	 * @return
	 */
	public GenericResponse getLibraryCollections(String id) {
		return getLibraryCollections(id, null);
	}

	/**
	 * GET collection/{id}
	 * 
	 * @param id
	 * @return
	 */
	public GenericResponse getCollection(String id) {
		return query("GET", "collection/" + id, null, null);
	}

	/**
	 * GET collection/{id}/items?page={page}
	 * 
	 * @param id
	 * @param page
	 * @return
	 */
	public GenericResponse getCollectionItems(String id, Integer page) {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("page", page != null ? page.toString() : null);
		return query("GET", "collection/" + id + "/items", parameters, null);
	}

	/**
	 * GET collection/{id}/items
	 * 
	 * @param id
	 * @return
	 */
	public GenericResponse getCollectionItems(String id) {
		return getCollectionItems(id, null);
	}

	/**
	 * GET collection/{collectionId}/item/{id}
	 * 
	 * @param collectionId
	 * @param id
	 * @return
	 */
	public GenericResponse getItem(String collectionId, String id) {
		return query("GET", "collection/" + collectionId + "/item/" + id, null, null);
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	private GenericResponse query(String httpVerb, String path, Map<String, String> parameters, String requestBody) {
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
			ClientResponse response = null;
			if ("GET".equals(httpVerb)) {
				response = builder.header("Authorization", "Bearer " + token.getToken()).get(ClientResponse.class);
			} else if ("POST".equals(httpVerb)) {
				response = builder.header("Authorization", "Bearer " + token.getToken()).post(ClientResponse.class, requestBody);
			} else if ("PUT".equals(httpVerb)) {
				response = builder.header("Authorization", "Bearer " + token.getToken()).put(ClientResponse.class, requestBody);
			} else if ("DELETE".equals(httpVerb)) {
				response = builder.header("Authorization", "Bearer " + token.getToken()).delete(ClientResponse.class, requestBody);
			} else {
				throw new IllegalArgumentException("Unknown httpVerb <" + httpVerb + "> !!!");
			}
			if (debug) {
				System.out.println(response);
			}
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
