package com.datatheke.restdriver;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.Date;

import org.junit.Test;

import com.datatheke.restdriver.beans.Collection;
import com.datatheke.restdriver.beans.FieldType;
import com.datatheke.restdriver.beans.Item;
import com.datatheke.restdriver.beans.type.LatLon;

public class DatathekeUtilsTest {

	@Test
	public void should_generate_collection() {
		Collection collection = DatathekeUtils.generateCollectionFor(AllTypes.class);
		assertThat(collection).isNotNull();
		assertThat(collection.getName()).isEqualTo("AllTypes");
		assertThat(collection.getFields()).isNotNull().hasSize(3);
		assertThat(collection.getField("Id")).isNotNull();
		assertThat(collection.getField("Id").getType()).isEqualTo(FieldType.string);
		assertThat(collection.getField("Date")).isNotNull();
		assertThat(collection.getField("Date").getType()).isEqualTo(FieldType.date);
		assertThat(collection.getField("Coordinates")).isNotNull();
		assertThat(collection.getField("Coordinates").getType()).isEqualTo(FieldType.coordinates);
	}

	@Test
	public void should_convert_to_item_and_get_back() {
		convert(new AllTypes("toto", new Date(), new LatLon(2.294619, 48.858207)));
		convert(new AllTypes("", new Date(0), new LatLon(0d, 0d)));
		convert(new AllTypes("toto", new Date(), new LatLon(null, null)));
		convert(new AllTypes(null, new Date(), new LatLon(2.294619, 48.858207)));
		convert(new AllTypes("toto", null, new LatLon(2.294619, 48.858207)));
		convert(new AllTypes("toto", new Date(), null));
		convert(new AllTypes(null, null, null));
		convert(new AllTypes());
	}

	private static void convert(AllTypes obj) {
		Collection collection = DatathekeUtils.generateCollectionFor(obj);
		Item item = DatathekeUtils.toItem(collection.getFields(), obj);
		AllTypes generatedObj = DatathekeUtils.fromItem(collection.getFields(), item, AllTypes.class);
		validate(obj, item, generatedObj);
		
		Item itemNoFields = DatathekeUtils.toItem(obj);
		AllTypes generatedObjNoFields = DatathekeUtils.fromItem(item, AllTypes.class);
		validate(obj, itemNoFields, generatedObjNoFields);
	}

	private static void validate(AllTypes obj, Item item, AllTypes generatedObj) {
		assertThat(item).isNotNull();
		assertThat(item.getValues()).isNotNull().hasSize(3);
		assertThat(item.getFieldValue("Id")).isEqualTo(obj.getId());
		assertThat(item.getFieldValue("Date")).isEqualTo(obj.getDate());
		assertThat(item.getFieldValue("Coordinates")).isEqualTo(obj.getCoordinates());

		assertThat(generatedObj).isEqualsToByComparingFields(obj);
	}

	private static class AllTypes {
		private String id;
		private Date date;
		private LatLon coordinates;

		public AllTypes() {
		}

		public AllTypes(String id, Date date, LatLon coordinates) {
			this.id = id;
			this.date = date;
			this.coordinates = coordinates;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public Date getDate() {
			return date;
		}

		public void setDate(Date date) {
			this.date = date;
		}

		public LatLon getCoordinates() {
			return coordinates;
		}

		public void setCoordinates(LatLon coordinates) {
			this.coordinates = coordinates;
		}

		@Override
		public String toString() {
			return "AllTypes [id=" + id + ", date=" + date + ", coordinates=" + coordinates + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((coordinates == null) ? 0 : coordinates.hashCode());
			result = prime * result + ((date == null) ? 0 : date.hashCode());
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			AllTypes other = (AllTypes) obj;
			if (coordinates == null) {
				if (other.coordinates != null)
					return false;
			} else if (!coordinates.equals(other.coordinates))
				return false;
			if (date == null) {
				if (other.date != null)
					return false;
			} else if (!date.equals(other.date))
				return false;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			return true;
		}
	}
}
