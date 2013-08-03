package com.datatheke.restdriver;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.datatheke.restdriver.beans.Collection;
import com.datatheke.restdriver.beans.Field;
import com.datatheke.restdriver.beans.FieldType;
import com.datatheke.restdriver.beans.Item;
import com.datatheke.restdriver.beans.utils.Pair;
import com.datatheke.restdriver.response.IdResponse;

public class DatathekeUtils {
	public static Collection generateCollectionFor(Class<?> clazz) {
		if (clazz != null) {
			return new Collection(null, clazz.getSimpleName(), null, generateValidFields(clazz));
		}
		return null;
	}

	public static Collection generateCollectionFor(Object obj) {
		if (obj != null) {
			return generateCollectionFor(obj.getClass());
		}
		return null;
	}

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
						e.printStackTrace();
					}
				}
			}
			return new Item(null, values);
		}
		return null;
	}

	public static Item toItem(Object obj) {
		if (obj != null) {
			return toItem(generateValidFields(obj.getClass()), obj);
		}
		return null;
	}

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
							e.printStackTrace();
						}
					}
				}
			} catch (InstantiationException e) {
				System.out.println(clazz + " must have an empty constructor !");
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

			return obj;
		}
		return null;
	}

	public static <T> T fromItem(Item item, Class<T> clazz) {
		return fromItem(generateValidFields(clazz), item, clazz);
	}

	public static String createCollection(DatathekeRestDriver driver, String libraryId, Object obj) {
		if (driver != null && libraryId != null && obj != null) {
			Collection collection = DatathekeUtils.generateCollectionFor(obj.getClass());
			collection.setDescription("Automatically generated collection");
			IdResponse createCollection = driver.createCollection(libraryId, collection);
			return createCollection.getId();
		}
		return null;
	}

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

	public static String createItem(DatathekeRestDriver driver, Collection collection, Object obj) {
		if (driver != null && collection != null && obj != null) {
			Item item = DatathekeUtils.toItem(collection.getFields(), obj);
			IdResponse createItem = driver.createItem(collection.getId(), item);
			return createItem.getId();
		}
		return null;
	}

	public static Pair<Collection, List<String>> createItems(DatathekeRestDriver driver, String collectionId, List<Object> objs) {
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

	public static Pair<Collection, List<String>> createItems(DatathekeRestDriver driver, String collectionId, Object... objs) {
		return createItems(driver, collectionId, Arrays.asList(objs));
	}

	public static Pair<Collection, String> createCollectionAndItem(DatathekeRestDriver driver, String libraryId, Object obj) {
		String collectionId = createCollection(driver, libraryId, obj);
		return createItem(driver, collectionId, obj);
	}

	public static Pair<Collection, List<String>> createCollectionAndItems(DatathekeRestDriver driver, String libraryId, List<Object> objs) {
		// TODO not working yet...
		// String collectionId = createCollection(driver, libraryId,
		// objs.get(0).getClass());
		// return createItems(driver, collectionId, objs);
		return null;
	}

	public static Pair<Collection, List<String>> createCollectionAndItems(DatathekeRestDriver driver, String libraryId, Object... objs) {
		// TODO not working yet
		// String collectionId = createCollection(driver, libraryId,
		// objs[0].getClass());
		// return createItems(driver, collectionId, objs);
		return null;
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
						System.out.println("Unable to generate field for class: " + method.getReturnType());
					}
				}
			}
		}
		return getters;
	}
}
