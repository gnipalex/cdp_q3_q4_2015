package com.epam.hnyp.springbooking.dao.storage.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.epam.hnyp.springbooking.dao.storage.Storage;

public class MapStorage implements Storage {

    private Map<String, Object> entries;
    
    public MapStorage() {
        entries = new ConcurrentHashMap<>();
    }

    @Override
    public boolean put(String key, Object value) {
        if (entries.containsKey(key)) {
            return false;
        }
        entries.put(key, value);
        return true;
    }

    @SuppressWarnings("unchecked")
	@Override
    public <T> T get(String key, Class<T> clazz) {
        Object value = entries.get(key);
        if (clazz.isInstance(value)) {
            return (T) value;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
	@Override
    public <T> List<T> getAllByPrefix(String prefix, Class<T> clazz) {
    	return entries.entrySet().stream()
            .filter(e -> e.getKey().startsWith(prefix))
            .filter(e -> clazz.isInstance(e.getValue()))
            .map(e -> (T) e.getValue())
            .collect(Collectors.toList());
    }

    @Override
    public boolean remove(String key) {
        return entries.remove(key) != null;
    }

}
