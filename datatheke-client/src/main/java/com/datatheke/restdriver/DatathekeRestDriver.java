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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datatheke.restdriver.bean.Collection;
import com.datatheke.restdriver.bean.Field;
import com.datatheke.restdriver.bean.Item;
import com.datatheke.restdriver.bean.Library;
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
	private static final Logger LOGGER = LoggerFactory.getLogger(DatathekeRestDriver.class);
	private static final ObjectMapper jsonMapper = new ObjectMapper().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
	public static final String DEFAULT_URI = "http://www.datatheke.com/api/v1";
	private final WebResource resource;
	private AuthenticateToken token;
	private boolean debug;

	public DatathekeRestDriver() {
		resource = Client.create().resource(DEFAULT_URI);
		debug = false;
	}

	public DatathekeRestDriver(String baseUri) {
		resource = Client.create().resource(baseUri);
		debug = false;
	}

	/**
	 * Test if the driver is connected and logged to the datatheke API
	 * 
	 * @return
	 */
	public Boolean isConnected() {
		return token != null;
	}

	/**
	 * Login to the datatheke API with the provided username and password. Check
	 * if it works with isConnected() method
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public DatathekeRestDriver authenticate(String username, String password) {
		resource.addFilter(new HTTPBasicAuthFilter(username, password));
		WebResource endpoint = resource.path("token");
		Builder builder = endpoint.accept(MediaType.APPLICATION_JSON_TYPE);
		ClientResponse response = builder.post(ClientResponse.class);
		if (debug) {
			LOGGER.info("{}", response);
		}
		String entity = response.getEntity(String.class);
		if (response.getStatus() == 200) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				token = mapper.readValue(entity, AuthenticateToken.class);
			} catch (JsonParseException e) {
				LOGGER.info("{}", e);
			} catch (JsonMappingException e) {
				LOGGER.info("{}", e);
			} catch (IOException e) {
				LOGGER.info("{}", e);
			}
		}
		return this;
	}

	/**
	 * Get a specific page in the list of your librairies. A page is 20
	 * elements.
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
	 * Get the first page of your librairies (20 elements max)
	 * 
	 * @return
	 */
	public LibrariesResponse getLibraries() {
		return getLibraries(null);
	}

	/**
	 * Get a libraray with it id
	 * 
	 * @param libraryId
	 * @return
	 */
	public LibraryResponse getLibrary(String libraryId) {
		return new LibraryResponse(query("GET", "library/" + libraryId, null, null));
	}

	/**
	 * Create a new library. Library object must have a null id !
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
			LOGGER.info("{}", e);
		}

		return new IdResponse(query("POST", "library", null, requestBody));
	}

	/**
	 * Update a library. Library object must have it id field filled.
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
			LOGGER.info("{}", e);
		}
		return new EmptyResponse(query("PUT", "library/" + library.getId(), null, requestBody));
	}

	/**
	 * Delete a library and all its content.
	 * 
	 * @param libraryId
	 * @return
	 */
	public EmptyResponse deleteLibrary(String libraryId) {
		return new EmptyResponse(query("DELETE", "library/" + libraryId, null, null));
	}

	/**
	 * Get a specific page of collections in the specified library. A page is 20
	 * elements.
	 * 
	 * @param libraryId
	 * @param page
	 * @return
	 */
	public CollectionsResponse getCollectionsForLibrary(String libraryId, Integer page) {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("page", page != null ? page.toString() : null);
		return new CollectionsResponse(query("GET", "library/" + libraryId + "/collections", parameters, null));
	}

	/**
	 * Get the first page of collections in the specified library (20 elements
	 * max)
	 * 
	 * @param libraryId
	 * @return
	 */
	public CollectionsResponse getCollectionsForLibrary(String libraryId) {
		return getCollectionsForLibrary(libraryId, null);
	}

	/**
	 * Get a collection with it id
	 * 
	 * @param collectionId
	 * @return
	 */
	public CollectionResponse getCollection(String collectionId) {
		return new CollectionResponse(query("GET", "collection/" + collectionId, null, null));
	}

	/**
	 * Create a new collection in the specified library. Collection object must
	 * have a null id !
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
			LOGGER.info("{}", e);
		}
		return new IdResponse(query("POST", "library/" + libraryId, null, requestBody));
	}

	/**
	 * Update a collection. Collection object must have it id field filled.
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
			LOGGER.info("{}", e);
		}
		return new EmptyResponse(query("PUT", "collection/" + collection.getId(), null, requestBody));
	}

	/**
	 * Delete a collection and all its content.
	 * 
	 * @param collectionId
	 * @return
	 */
	public EmptyResponse deleteCollection(String collectionId) {
		return new EmptyResponse(query("DELETE", "collection/" + collectionId, null, null));
	}

	/**
	 * Get a specific page of items in the specified collection. A page is 20
	 * elements.
	 * 
	 * @param collection
	 * @param page
	 * @return
	 */
	public ItemsResponse getItemsForCollection(Collection collection, Integer page) {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("page", page != null ? page.toString() : null);
		return new ItemsResponse(query("GET", "collection/" + collection.getId() + "/items", parameters, null), collection.getFields());
	}

	/**
	 * Get the first page of items in the specified collection (20 elements max)
	 * 
	 * @param collection
	 * @return
	 */
	public ItemsResponse getItemsForCollection(Collection collection) {
		return getItemsForCollection(collection, null);
	}

	/**
	 * Get an item with it collection and it id
	 * 
	 * @param collectionId
	 * @param itemId
	 * @return
	 */
	public ItemResponse getItem(Collection collection, String itemId) {
		return new ItemResponse(query("GET", "collection/" + collection.getId() + "/item/" + itemId, null, null), collection.getFields());
	}

	/**
	 * Create a new item in the specified collection. Item object must have a
	 * null id !
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
			LOGGER.info("{}", e);
		}
		return new IdResponse(query("POST", "collection/" + collectionId, null, requestBody));
	}

	/**
	 * Update an item. Item object must have it id field filled.
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
			LOGGER.info("{}", e);
		}
		LOGGER.info("{}", requestBody);
		return new EmptyResponse(query("PUT", "collection/" + collectionId + "/item/" + item.getId(), null, requestBody));
	}

	/**
	 * Delete an item.
	 * 
	 * @param collectionId
	 * @param itemId
	 * @return
	 */
	public EmptyResponse deleteItem(String collectionId, String itemId) {
		// FIXME get an internal serveur error (500) !
		return new EmptyResponse(query("DELETE", "collection/" + collectionId + "/item/" + itemId, null, null));
	}

	/**
	 * Set debug mode to true to see in logs all calls made to the datatheke API
	 * 
	 * @param debug
	 */
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
				LOGGER.info("{}", response);
			}
			return response;
		} else {
			throw new IllegalStateException("You must be authenticated before perform this action !!!");
		}
	}
}
