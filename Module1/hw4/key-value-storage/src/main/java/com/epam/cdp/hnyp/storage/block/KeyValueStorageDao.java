package com.epam.cdp.hnyp.storage.block;

public interface KeyValueStorageDao {
    Object put(String key, Object value);
    boolean create(String key, Object value);
    Object read(String key);
    void update(String key, Object value);
    boolean delete(String key);
}
