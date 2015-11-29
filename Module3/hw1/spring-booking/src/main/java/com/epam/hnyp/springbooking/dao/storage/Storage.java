package com.epam.hnyp.springbooking.dao.storage;

import java.util.List;

public interface Storage {
    boolean put(String key, Object value);
    boolean remove(String key);
    <T> T get(String key, Class<T> clazz);
    <T> List<T> getAllByPrefix(String prefix, Class<T> clazz);
}
