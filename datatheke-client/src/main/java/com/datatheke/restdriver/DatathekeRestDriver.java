package com.datatheke.restdriver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.datatheke.restdriver.beans.Collection;
import com.datatheke.restdriver.beans.Field;
import com.datatheke.restdriver.beans.Item;
import com.datatheke.restdriver.beans.Library;
import com.datatheke.restdriver.response.AuthenticateToken;
import com.datatheke.restdriver.response.CollectionResponse;
import com.datatheke.restdriver.response.CollectionsResponse;
import com.datatheke.restdriver.response.EmptyResponse;
import com.datatheke.restdriver.response.IdResponse;
import com.datatheke.restdriver.response.ItemResponse;
import com.datatheke.restdriver.response.ItemsResponse;
import com.datatheke.restdriver.response.LibrariesResponse;
import com.datatheke.restdriver.response.LibraryResponse;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

public class DatathekeRestDriver {
	private static final ObjectMapper jsonMapper = new ObjectMapper().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
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
	public LibrariesResponse getLibraries(Integer page) {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("page", page != null ? page.toString() : null);
		return new LibrariesResponse(query("GET", "libraries", parameters, null));
	}

	/**
	 * GET librairies
	 * 
	 * @return
	 */
	public LibrariesResponse getLibraries() {
		return getLibraries(null);
	}

	/**
	 * GET library/{id}
	 * 
	 * @param id
	 * @return
	 */
	public LibraryResponse getLibrary(String id) {
		return new LibraryResponse(query("GET", "library/" + id, null, null));
	}

	/**
	 * call url POST library<br/>
	 * get response: {"id":"51fb76d6138a76d45500035f"}
	 * 
	 * @param library
	 * @return id of the created library
	 */
	public IdResponse createLibrary(Library library) {
		if (library == null) {
			throw new IllegalArgumentException("Library can't be null !!!");
		}
		if (library.getId() != null) {
			throw new IllegalArgumentException("Can't create a Library with an id !!!");
		}
		String requestBody = null;
		try {
			Map<String, Library> map = new HashMap<String, Library>();
			map.put("library", library);
			requestBody = jsonMapper.writeValueAsString(map);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new IdResponse(query("POST", "library", null, requestBody));
	}

	/**
	 * PUT library/{id}
	 * 
	 * @param library
	 * @return
	 */
	public EmptyResponse updateLibrary(Library library) {
		if (library == null || library.getId() == null || library.getId().length() == 0) {
			throw new IllegalArgumentException("Library can't be null and must have a non-empty id : " + library);
		}
		String requestBody = null;
		try {
			Map<String, Library> map = new HashMap<String, Library>();
			map.put("library", new Library(null, library.getName(), library.getDescription()));
			requestBody = jsonMapper.writeValueAsString(map);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new EmptyResponse(query("PUT", "library/" + library.getId(), null, requestBody));
	}

	/**
	 * DELETE library/{id}
	 * 
	 * @param id
	 * @return
	 */
	public EmptyResponse deleteLibrary(String id) {
		return new EmptyResponse(query("DELETE", "library/" + id, null, null));
	}

	/**
	 * GET library/{id}/collections?page={page}
	 * 
	 * @param id
	 * @param page
	 * @return
	 */
	public CollectionsResponse getLibraryCollections(String id, Integer page) {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("page", page != null ? page.toString() : null);
		return new CollectionsResponse(query("GET", "library/" + id + "/collections", parameters, null));
	}

	/**
	 * GET library/{id}/collections
	 * 
	 * @param id
	 * @return
	 */
	public CollectionsResponse getCollectionsForLibrary(String id) {
		return getLibraryCollections(id, null);
	}

	/**
	 * GET collection/{id}
	 * 
	 * @param id
	 * @return
	 */
	public CollectionResponse getCollection(String id) {
		return new CollectionResponse(query("GET", "collection/" + id, null, null));
	}

	/**
	 * POST library/{id}<br/>
	 * get response: {"id":"51fb7aa5138a76255b000165"}
	 * 
	 * @param libraryId
	 * @param collection
	 * @return
	 */
	public IdResponse createCollection(String libraryId, Collection collection) {
		if (collection == null) {
			throw new IllegalArgumentException("Collection can't be null !!!");
		}
		if (collection.getId() != null) {
			throw new IllegalArgumentException("Can't create a Collection with an id !!!");
		}
		String requestBody = null;
		try {
			Map<String, Collection> map = new HashMap<String, Collection>();
			map.put("collection", collection);
			requestBody = jsonMapper.writeValueAsString(map);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new IdResponse(query("POST", "library/" + libraryId, null, requestBody));
	}

	/**
	 * PUT collection/{id}
	 * 
	 * @param collection
	 * @return
	 */
	public EmptyResponse updateCollection(Collection collection) {
		// FIXME not working...
		if (collection == null || collection.getId() == null || collection.getId().length() == 0) {
			throw new IllegalArgumentException("Collection can't be null and must have a non-empty id : " + collection);
		}
		String requestBody = null;
		try {
			Map<String, Collection> map = new HashMap<String, Collection>();
			List<Field> fields = null;
			if (collection.getFields() != null) {
				fields = new ArrayList<Field>();
				for (Field field : collection.getFields()) {
					fields.add(new Field(null, field.getLabel(), field.getType()));
				}
			}
			map.put("collection", new Collection(null, collection.getName(), collection.getDescription(), fields));
			requestBody = jsonMapper.writeValueAsString(map);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("requestBody: " + requestBody);
		return new EmptyResponse(query("PUT", "collection/" + collection.getId(), null, requestBody));
	}

	/**
	 * DELETE collection/{id}
	 * 
	 * @param id
	 * @return
	 */
	public EmptyResponse deleteCollection(String id) {
		return new EmptyResponse(query("DELETE", "collection/" + id, null, null));
	}

	/**
	 * GET collection/{id}/items?page={page}
	 * 
	 * @param id
	 * @param page
	 * @return
	 */
	public ItemsResponse getItemsForCollection(Collection collection, Integer page) {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("page", page != null ? page.toString() : null);
		return new ItemsResponse(query("GET", "collection/" + collection.getId() + "/items", parameters, null), collection.getFields());
	}

	/**
	 * GET collection/{id}/items
	 * 
	 * @param id
	 * @return
	 */
	public ItemsResponse getItemsForCollection(Collection collection) {
		return getItemsForCollection(collection, null);
	}

	/**
	 * GET collection/{collectionId}/item/{id}
	 * 
	 * @param collectionId
	 * @param id
	 * @return
	 */
	public ItemResponse getItem(Collection collection, String id) {
		return new ItemResponse(query("GET", "collection/" + collection.getId() + "/item/" + id, null, null), collection.getFields());
	}

	/**
	 * POST collection/{id}
	 * 
	 * @param collectionId
	 * @param item
	 * @return
	 */
	public IdResponse createItem(String collectionId, Item item) {
		if (item == null) {
			throw new IllegalArgumentException("Item can't be null !!!");
		}
		if (item.getId() != null) {
			throw new IllegalArgumentException("Can't create a Item with an id !!!");
		}
		String requestBody = null;
		try {
			Map<String, Object> fieldMap = new HashMap<String, Object>();
			for (Entry<Field, Object> entry : item.getValues().entrySet()) {
				fieldMap.put("_" + entry.getKey().getId(), entry.getValue());
			}

			Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();
			map.put("_" + collectionId, fieldMap);
			requestBody = jsonMapper.writeValueAsString(map);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new IdResponse(query("POST", "collection/" + collectionId, null, requestBody));
	}

	/**
	 * PUT collection/{collectionId}/item/{id}
	 * 
	 * @param item
	 * @return
	 */
	public EmptyResponse updateItem(String collectionId, Item item) {
		if (item == null || item.getId() == null || item.getId().length() == 0) {
			throw new IllegalArgumentException("Item can't be null and must have a non-empty id : " + item);
		}
		String requestBody = null;
		try {
			Map<String, Object> fieldMap = new HashMap<String, Object>();
			for (Entry<Field, Object> entry : item.getValues().entrySet()) {
				fieldMap.put("_" + entry.getKey().getId(), entry.getValue());
			}

			Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();
			map.put("_" + collectionId, fieldMap);
			requestBody = jsonMapper.writeValueAsString(map);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(requestBody);
		return new EmptyResponse(query("PUT", "collection/" + collectionId + "/item/" + item.getId(), null, requestBody));
	}

	/**
	 * DELETE collection/{collectionId}/item/{id}
	 * 
	 * @param collectionId
	 * @param id
	 * @return
	 */
	public EmptyResponse deleteItem(String collectionId, String id) {
		// FIXME get an internal serveur error (500) !
		return new EmptyResponse(query("DELETE", "collection/" + collectionId + "/item/" + id, null, null));
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	private ClientResponse query(String httpVerb, String path, Map<String, String> parameters, String requestBody) {
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
				response = builder.header("Authorization", "Bearer " + token.getToken()).header("Content-type", "application/json")
						.post(ClientResponse.class, requestBody);
			} else if ("PUT".equals(httpVerb)) {
				response = builder.header("Authorization", "Bearer " + token.getToken()).header("Content-type", "application/json")
						.put(ClientResponse.class, requestBody);
			} else if ("DELETE".equals(httpVerb)) {
				response = builder.header("Authorization", "Bearer " + token.getToken()).delete(ClientResponse.class, requestBody);
			} else {
				throw new IllegalArgumentException("Unknown httpVerb <" + httpVerb + "> !!!");
			}
			if (debug) {
				System.out.println(response);
			}
			return response;
		} else {
			throw new IllegalStateException("You must be authenticated before perform this action !!!");
		}
	}
}
