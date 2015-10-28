package com.epam.cdp.hnyp.storage;

import java.io.Closeable;

import com.epam.cdp.hnyp.storage.exception.StorageException;

public interface KeyValueStorage extends Closeable {
    boolean create(String key, Object value) throws StorageException;
    Object read(String key) throws StorageException;
    boolean update(String key, Object value) throws StorageException;
    boolean delete(String key) throws StorageException;
}
