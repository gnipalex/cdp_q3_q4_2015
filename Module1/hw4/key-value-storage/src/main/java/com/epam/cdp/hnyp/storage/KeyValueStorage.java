package com.epam.cdp.hnyp.storage;

import java.io.Closeable;

import com.epam.cdp.hnyp.storage.exception.StorageException;

public interface KeyValueStorage extends Closeable {
    
	/**
     * Creates new key and value in storage
     * @return true if key and value created and saved
     * @throws StorageException in case of storage errors
     * @throws IllegalArgumentException if key is empty or value is null 
     */
    boolean create(String key, Object value) throws StorageException;
    
    /**
     * Reads value associated to key
     * @param key
     * @return value
     * @throws StorageException in case of storage errors
     * @throws IllegalArgumentException if key is empty
     */
    Object read(String key) throws StorageException;
    
    /**
     * Updates existing value
     * @param key
     * @param value
     * @return true if value updated, false if values doesn't exist
     * @throws StorageException in case of storage errors, if failed to update key or value
     * @throws IllegalArgumentException if key is empty or value is null 
     */
    boolean update(String key, Object value) throws StorageException;
    
    /**
     * Deletes key from key storage. 
     * @param key
     * @return true if key deleted
     * @throws StorageException in case of storage errors
     * @throws IllegalArgumentException if key is empty
     */
    boolean delete(String key) throws StorageException;
}
