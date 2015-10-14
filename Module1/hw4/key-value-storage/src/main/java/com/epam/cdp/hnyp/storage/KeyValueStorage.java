package com.epam.cdp.hnyp.storage;

public interface KeyValueStorage {
    boolean create(String key, Object value);
    Object read(String key);
    boolean update(String key, Object value);
    boolean delete(String key);
}
