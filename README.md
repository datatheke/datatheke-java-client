# Datatheke.com Java API client

A project which help to connect to [Datatheke](http://www.datatheke.com/) REST API with Java.

**Contributions and Feedback wanted !!!**

# Usage

You can see how to use this client in both project samples and client unit tests.
For a quick overview :
```java
// connexion to Datatheke api
DatathekeClient driver = new DatathekeClient();
driver.authenticate("myDatathekeUsername", "myDatathekePassword");

// create a library
Library library = new Library("My first library", "A sample test library with this really short description.");
IdResponse createLibrary = driver.createLibrary(library);
String libraryId = createLibrary.getId();

// create a collection inside created library
Collection collection = new Collection("My first collection", "A sample test collection");
collection.addField(new Field("key", FieldType.string));
collection.addField(new Field("value", FieldType.string));
IdResponse createCollection = driver.createCollection(libraryId, collection);
String collectionId = createCollection.getId();

// create an item into the created collection
CollectionResponse collectionResponse = driver.getCollection(collectionId);
if (collectionResponse.isStatusOk()) {
	Collection collection = collectionResponse.getOrNull();
	// set item fields : they must match with collection fields
	Item item = new Item();
	item.addField(collection.getField("key"), "key"); // item fields must have their datatheke id
	item.addField(collection.getField("value"), "value");
	IdResponse createItem = driver.createItem(collectionId, item);
	if (createItem.isStatusOk()) {
		return createItem.getId();
	}
}
```
