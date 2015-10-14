package com.epam.cdp.hnyp.storage.key;

import java.io.Closeable;

import com.epam.cdp.hnyp.storage.exception.StorageException;

public interface KeyStorage extends Closeable {
    KeyDescriptor create(String key, int valueLength) throws StorageException;
    KeyDescriptor read(String key);
    boolean update(KeyDescriptor keyDescriptor) throws StorageException;
    boolean delete(String key) throws StorageException;
    
}
