package com.datatheke.restdriver;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datatheke.restdriver.bean.Collection;
import com.datatheke.restdriver.bean.Field;
import com.datatheke.restdriver.bean.FieldType;
import com.datatheke.restdriver.bean.Item;
import com.datatheke.restdriver.bean.util.Pair;
import com.datatheke.restdriver.response.IdResponse;

public class DatathekeUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(DatathekeUtils.class);

	/**
	 * Create a collection object with fields based on public getters of the
	 * requested class
	 * 
	 * @param clazz
	 * @return
	 */
	public static Collection generateCollectionFor(Class<?> clazz) {
		if (clazz != null) {
			return new Collection(null, clazz.getSimpleName(), null, generateValidFields(clazz));
		}
		return null;
	}

	/**
	 * Create a collection object with fields based on public getters of the
	 * requested object.<br/>
	 * This method simply call generateCollectionFor(Class&lt;?&gt; clazz) with
	 * a obj.getClass()
	 * 
	 * @param clazz
	 * @return
	 */
	public static Collection generateCollectionFor(Object obj) {
		if (obj != null) {
			return generateCollectionFor(obj.getClass());
		}
		return null;
	}

	/**
	 * Create an item object with fields passed in argument and values based on
	 * obj attributes. Field labels must be named like obj attributes<br/>
	 * Ex: for a field with label <b>Id</b>, this method will try to get the
	 * value from <b>getId()</b>
	 * 
	 * @param fields
	 * @param obj
	 * @return
	 */
	public static Item toItem(List<Field> fields, Object obj) {
		if (obj != null) {
			Map<Field, Object> values = new HashMap<Field, Object>();
			Class<? extends Object> clazz = obj.getClass();

			if (fields != null) {
				for (Field field : fields) {
					try {
						Method method = clazz.getMethod("get" + field.getLabel());
						Object value = method.invoke(obj);
						values.put(field, value);
					} catch (Exception e) {
						LOGGER.info("{}", e);
					}
				}
			}
			return new Item(null, values);
		}
		return null;
	}

	/**
	 * Create an item with fields based on public getters of the requested
	 * object. To save Item to a datatheke collection prefer to use
	 * <b>toItem(List&lt;Field&gt; fields, Object obj)</b> with fields of the
	 * retrieved collection from API (they will have needed ids !)
	 * 
	 * @param obj
	 * @return
	 */
	public static Item toItem(Object obj) {
		if (obj != null) {
			return toItem(generateValidFields(obj.getClass()), obj);
		}
		return null;
	}

	/**
	 * Create a T class from an Item based on the fields. This is the mirror
	 * method of toItem(List&lt;Field&gt; fields, Object obj).<br/>
	 * T class must have an <b>empty constructor</b> and the appropriate
	 * setters.
	 * 
	 * @param fields
	 * @param item
	 * @param clazz
	 * @return
	 */
	public static <T> T fromItem(List<Field> fields, Item item, Class<T> clazz) {
		if (item != null && clazz != null) {
			T obj = null;
			try {
				obj = clazz.newInstance();

				if (fields != null) {
					for (Field field : fields) {
						try {
							Method method = clazz.getMethod("set" + field.getLabel(), FieldType.get(field.getType()));
							method.invoke(obj, item.getFieldValue(field));
						} catch (Exception e) {
							LOGGER.info("{}", e);
						}
					}
				}
			} catch (InstantiationException e) {
				LOGGER.info("{} must have an empty constructor !", clazz, e);
			} catch (IllegalAccessException e) {
				LOGGER.info("{}", e);
			}

			return obj;
		}
		return null;
	}

	/**
	 * Create a T class from an Item based on its public getters. If you get the
	 * item from a datatheke collection prefer the method
	 * fromItem(List&lt;Field&gt; fields, Item item, Class&lt;?&gt; clazz) using
	 * the collection fields.
	 * 
	 * @param item
	 * @param clazz
	 * @return
	 */
	public static <T> T fromItem(Item item, Class<T> clazz) {
		return fromItem(generateValidFields(clazz), item, clazz);
	}

	/**
	 * Create a collection based in the Class clazz and save it directly to
	 * datatheke
	 * 
	 * @param driver
	 * @param libraryId
	 * @param obj
	 * @return Id of the created collection
	 */
	public static String createCollection(DatathekeRestDriver driver, String libraryId, Class<?> clazz) {
		if (driver != null && libraryId != null && clazz != null) {
			Collection collection = DatathekeUtils.generateCollectionFor(clazz);
			collection.setDescription("Automatically generated collection");
			IdResponse createCollection = driver.createCollection(libraryId, collection);
			return createCollection.getId();
		}
		return null;
	}

	/**
	 * This method is simply a help based on
	 * createCollection(DatathekeRestDriver driver, String libraryId,
	 * Class&lt;?&gt; clazz)
	 * 
	 * @param driver
	 * @param libraryId
	 * @param obj
	 * @return
	 */
	public static String createCollection(DatathekeRestDriver driver, String libraryId, Object obj) {
		if (obj != null) {
			return createCollection(driver, libraryId, obj.getClass());
		}
		return null;
	}

	/**
	 * Create an Item based on obj and save it to datatheke in the specified
	 * collection. Collection and obj must match (number, names and types of
	 * fields) if you want to do the right job.
	 * 
	 * @param driver
	 * @param collectionId
	 * @param obj
	 * @return the collection retrieved form datatheke and the inserted item id
	 */
	public static Pair<Collection, String> createItem(DatathekeRestDriver driver, String collectionId, Object obj) {
		if (driver != null && collectionId != null && obj != null) {
			Collection collection = driver.getCollection(collectionId).getOrNull();
			if (collection != null) {
				Item item = DatathekeUtils.toItem(collection.getFields(), obj);
				IdResponse createItem = driver.createItem(collection.getId(), item);
				return new Pair<Collection, String>(collection, createItem.getId());
			}
		}
		return new Pair<Collection, String>();
	}

	/**
	 * Same as createItem(DatathekeRestDriver driver, String collectionId,
	 * Object obj) but with a collection already fetched from datatheke.
	 * 
	 * @param driver
	 * @param collection
	 * @param obj
	 * @return the created item id
	 */
	public static String createItem(DatathekeRestDriver driver, Collection collection, Object obj) {
		if (driver != null && collection != null && obj != null) {
			Item item = DatathekeUtils.toItem(collection.getFields(), obj);
			IdResponse createItem = driver.createItem(collection.getId(), item);
			return createItem.getId();
		}
		return null;
	}

	/**
	 * Same as createItem(DatathekeRestDriver driver, String collectionId,
	 * Object obj) but with a list of elements.
	 * 
	 * @param driver
	 * @param collectionId
	 * @param objs
	 * @return the collection retrieved form datatheke and the list of inserted
	 *         item id
	 */
	public static Pair<Collection, List<String>> createItems(DatathekeRestDriver driver, String collectionId, List<? extends Object> objs) {
		if (driver != null && collectionId != null && objs != null) {
			Collection collection = driver.getCollection(collectionId).getOrNull();
			if (collection != null) {
				List<String> ids = new ArrayList<String>();
				for (Object obj : objs) {
					Item item = DatathekeUtils.toItem(collection.getFields(), obj);
					IdResponse createItem = driver.createItem(collection.getId(), item);
					ids.add(createItem.getId());
				}
				return new Pair<Collection, List<String>>(collection, ids);
			}
		}
		return new Pair<Collection, List<String>>(null, new ArrayList<String>());
	}

	/**
	 * Same as createItems(DatathekeRestDriver driver, String collectionId,
	 * List&lt;Object&gt; objs)
	 * 
	 * @param driver
	 * @param collectionId
	 * @param objs
	 * @return
	 */
	public static Pair<Collection, List<String>> createItems(DatathekeRestDriver driver, String collectionId, Object... objs) {
		return createItems(driver, collectionId, Arrays.asList(objs));
	}

	/**
	 * For the specified obj, this method create the appropriate collection and
	 * save obj in the created collection.
	 * 
	 * @param driver
	 * @param libraryId
	 * @param obj
	 * @return the collection created and the id of created item
	 */
	public static Pair<Collection, String> createCollectionAndItem(DatathekeRestDriver driver, String libraryId, Object obj) {
		String collectionId = createCollection(driver, libraryId, obj);
		return createItem(driver, collectionId, obj);
	}

	/**
	 * Same as createCollectionAndItem(DatathekeRestDriver driver, String
	 * libraryId, Object obj) but wuth a list of objs<br/>
	 * <b>WARNING</b>: not implemented !!!
	 * 
	 * @param driver
	 * @param libraryId
	 * @param objs
	 * @return
	 */
	public static Pair<Collection, List<String>> createCollectionAndItems(DatathekeRestDriver driver, String libraryId, List<Object> objs) {
		// TODO not working yet...
		// String collectionId = createCollection(driver, libraryId,
		// objs.get(0).getClass());
		// return createItems(driver, collectionId, objs);
		return null;
	}

	/**
	 * Same as createCollectionAndItems(DatathekeRestDriver driver, String
	 * libraryId, List&lt;Object&gt; objs)<br/>
	 * <b>WARNING</b>: not implemented !!!
	 * 
	 * @param driver
	 * @param libraryId
	 * @param objs
	 * @return
	 */
	public static Pair<Collection, List<String>> createCollectionAndItems(DatathekeRestDriver driver, String libraryId, Object... objs) {
		return createCollectionAndItems(driver, libraryId, objs);
	}

	private static List<Field> generateValidFields(Class<?> clazz) {
		List<Field> fields = new ArrayList<Field>();
		for (Method method : getValidGetters(clazz)) {
			FieldType fieldType = FieldType.get(method.getReturnType());
			String label = method.getName().substring(3);
			fields.add(new Field(null, label, fieldType));
		}
		return fields;
	}

	private static List<Method> getValidGetters(Class<?> clazz) {
		List<Method> getters = new ArrayList<Method>();
		if (clazz != null) {
			Method[] methods = clazz.getMethods();
			for (Method method : methods) {
				if (method != null && method.getName() != null && method.getName().startsWith("get")
						&& !method.getName().equals("getClass")) {
					FieldType fieldType = FieldType.get(method.getReturnType());
					if (fieldType != null) {
						getters.add(method);
					} else {
						LOGGER.info("Unable to generate field for class: {}", method.getReturnType());
					}
				}
			}
		}
		return getters;
	}
}
