package com.epam.hnyp.springbooking.dao.storage.impl;

import static org.fest.assertions.Assertions.assertThat;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class MapStorageTest {

	private static final String ENTRIES_FIELD = "entries";

	private static final String KEY = "key";
	private static final String PREFIX = "prefix";
	
	private static final Object OBJECT_VALUE = new Object();
	private static final String STRING_VALUE = "string";
	private static final Integer INTEGER_VALUE = 22;
	private static final Double DOUBLE_VALUE = 5D;
	
	private MapStorage mapStorage;
	
	@Before
	public void setUp() {
		mapStorage = new MapStorage();
	}
	
	@Test
	public void shouldStoreValue_whenPerformPut() throws Exception {
		boolean isStored = mapStorage.put(KEY, OBJECT_VALUE);
		Object storedValue = getValueFromInternalMap(KEY);
		assertThat(isStored).isTrue();
		assertThat(storedValue).isEqualTo(OBJECT_VALUE);
	}
	
	private Object getValueFromInternalMap(String key) throws Exception {
		Map<String, Object> internalMap = getPrivateField(mapStorage, ENTRIES_FIELD);
		return internalMap.get(KEY);
	}
	
	@SuppressWarnings("unchecked")
	private <T> T getPrivateField(Object object, String fieldName) throws Exception {
		Field field = object.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		return (T) field.get(object);
	}
	
	@Test
	public void shouldNotStoreValue_whenPerformPutWithExistingKey() throws Exception {
		mapStorage.put(KEY, OBJECT_VALUE);
		boolean isStored = mapStorage.put(KEY, STRING_VALUE);
		Object storedValue = getValueFromInternalMap(KEY);
		assertThat(isStored).isFalse();
		assertThat(storedValue).isEqualTo(OBJECT_VALUE);
	}
	
	@Test
	public void shouldReturnStoredValue_whenPerformGetValueIsStoredAndValueClassMatches() {
		mapStorage.put(KEY, STRING_VALUE);
		String storedValue = mapStorage.get(KEY, String.class);
		assertThat(storedValue).isEqualTo(STRING_VALUE);
	}
	
	@Test
	public void shouldReturnNull_whenPerformGetValueIsStoredAndValueClassDoesNotMatch() {
		mapStorage.put(KEY, STRING_VALUE);
		Integer storedValue = mapStorage.get(KEY, Integer.class);
		assertThat(storedValue).isNull();
	}
	
	@Test
	public void shouldReturnNull_whenPerformGetAndStorageDoesNotContainValue() {
		Integer storedValue = mapStorage.get(KEY, Integer.class);
		assertThat(storedValue).isNull();
	}
	
	@Test
	public void shouldReturnAllMatchedElements_whenPerformGetAllByPrefix() {
		mapStorage.put(PREFIX + 1, STRING_VALUE);
		mapStorage.put(PREFIX + 2, INTEGER_VALUE);
		mapStorage.put(PREFIX + 3, DOUBLE_VALUE);
		List<Number> storedNumbersList = mapStorage.getAllByPrefix(PREFIX, Number.class);
		assertThat(storedNumbersList).containsOnly(INTEGER_VALUE, DOUBLE_VALUE);
	}
	
	@Test
	public void shouldRemoveStoredValue_whenPerformRemoveAndStorageContainsValue() {
		mapStorage.put(KEY, STRING_VALUE);
		boolean isRemoved = mapStorage.remove(KEY);
		assertThat(isRemoved).isTrue();
		Object storedValue = mapStorage.get(KEY, Object.class);
		assertThat(storedValue).isNull();
	}

}
