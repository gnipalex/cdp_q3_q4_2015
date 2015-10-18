package com.epam.cdp.hnyp.storage;

import java.io.Closeable;

public interface KeyValueStorage extends Closeable {
    boolean create(String key, Object value);
    Object read(String key);
    boolean update(String key, Object value);
    boolean delete(String key);
}
