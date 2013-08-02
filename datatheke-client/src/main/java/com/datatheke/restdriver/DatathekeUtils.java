package com.datatheke.restdriver;

import java.lang.reflect.InvocationTargetException;
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
		List<Field> fields = new ArrayList<Field>();

		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			if (method != null && method.getName() != null && method.getName().startsWith("get") && !method.getName().equals("getClass")) {
				FieldType fieldType = FieldType.get(method.getReturnType());
				String label = method.getName().substring(3);
				if (fieldType != null) {
					fields.add(new Field(null, label, fieldType));
				} else {
					System.out.println("Unable to generate field for class: " + method.getReturnType());
				}
			}
		}

		return new Collection(null, clazz.getCanonicalName(), null, fields);
	}

	public static Collection generateCollectionFor(Object obj) {
		return generateCollectionFor(obj.getClass());
	}

	public static Item toItem(List<Field> fields, Object obj) {
		Map<Field, Object> values = new HashMap<Field, Object>();
		Class<? extends Object> clazz = obj.getClass();

		for (Field field : fields) {
			try {
				Method method = clazz.getMethod("get" + field.getLabel());
				Object value = method.invoke(obj);
				values.put(field, value);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}

		return new Item(null, values);
	}

	public static <T> T fromItem(List<Field> fields, Item item, Class<T> clazz) {
		// TODO
		return null;
	}

	public static String createCollection(DatathekeRestDriver driver, String libraryId, Object obj) {
		Collection collection = DatathekeUtils.generateCollectionFor(obj.getClass());
		collection.setDescription("Automatically generated collection");
		IdResponse createCollection = driver.createCollection(libraryId, collection);
		return createCollection.getId();
	}

	public static Pair<Collection, String> createItem(DatathekeRestDriver driver, String collectionId, Object obj) {
		Collection collection = driver.getCollection(collectionId).getOrNull();
		if (collection != null) {
			Item item = DatathekeUtils.toItem(collection.getFields(), obj);
			IdResponse createItem = driver.createItem(collection.getId(), item);
			return new Pair<Collection, String>(collection, createItem.getId());
		} else {
			return new Pair<Collection, String>();
		}
	}

	public static String createItem(DatathekeRestDriver driver, Collection collection, Object obj) {
		Item item = DatathekeUtils.toItem(collection.getFields(), obj);
		IdResponse createItem = driver.createItem(collection.getId(), item);
		return createItem.getId();
	}

	public static Pair<Collection, List<String>> createItems(DatathekeRestDriver driver, String collectionId, List<Object> objs) {
		Collection collection = driver.getCollection(collectionId).getOrNull();
		if (collection != null) {
			List<String> ids = new ArrayList<String>();
			for (Object obj : objs) {
				Item item = DatathekeUtils.toItem(collection.getFields(), obj);
				IdResponse createItem = driver.createItem(collection.getId(), item);
				ids.add(createItem.getId());
			}
			return new Pair<Collection, List<String>>(collection, ids);
		} else {
			return new Pair<Collection, List<String>>(null, new ArrayList<String>());
		}
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
}
