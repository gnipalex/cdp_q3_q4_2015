package com.epam.cdp.hnyp.storage.impl;

import java.io.Closeable;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.epam.cdp.hnyp.storage.KeyValueStorage;
import com.epam.cdp.hnyp.storage.exception.StorageException;
import com.epam.cdp.hnyp.storage.key.KeyDescriptor;
import com.epam.cdp.hnyp.storage.key.KeyStorage;
import com.epam.cdp.hnyp.storage.value.ValueStorage;

public class DefaultKeyValueStorage implements KeyValueStorage {

    private static final Logger LOG = Logger.getLogger(DefaultKeyValueStorage.class);
    
    private KeyStorage keyStorage;
    private ValueStorage valueStorage;
 
    public DefaultKeyValueStorage() {
    }
    
    public DefaultKeyValueStorage(KeyStorage keyStorage, ValueStorage valueStorage) {
        this.keyStorage = keyStorage;
        this.valueStorage = valueStorage;
    }
    
    @Override
    public boolean create(String key, Object value) throws StorageException {
        checkKeyIsNotEmpty(key);
        checkValueIsNotNull(value);
        try {
            int valueLength = valueStorage.calculateLength(value);
            KeyDescriptor createdDescriptor = 
                    keyStorage.create(key, valueLength, value.getClass());
            if (createdDescriptor == null) {
                return false;
            }
            try {
                valueStorage.writeValue(createdDescriptor, value);
            } catch (StorageException e) {
                LOG.error(MessageFormat.format("value writing failed, deleting key {0}", key), e);
                keyStorage.delete(key);
                throw e;
            }
        } catch (StorageException e) {
            LOG.error(MessageFormat.format("failed to store key {0}", key), e);
            throw e;
        }
        return true;
    }
    
    private void checkKeyIsNotEmpty(String key) {
        if (StringUtils.isEmpty(key)) {
            throw new IllegalArgumentException("key should not be empty");
        }
    }
    
    private void checkValueIsNotNull(Object value) {
        if (value == null) {
            throw new IllegalArgumentException("value should not be null");
        }
    }

    @Override
    public Object read(String key) throws StorageException {
        checkKeyIsNotEmpty(key);
        KeyDescriptor keyDescriptor = keyStorage.read(key);
        if (keyDescriptor == null) {
            return null;
        }
        try {
            return valueStorage.readValue(keyDescriptor);
        } catch (StorageException e) {
            LOG.error(MessageFormat.format("error while reading key {0}", key), e);
            throw e;
        }
    }

    @Override
    public boolean update(String key, Object value) throws StorageException {
        checkKeyIsNotEmpty(key);
        checkValueIsNotNull(value);
        KeyDescriptor oldDescriptor = keyStorage.read(key);
        if (oldDescriptor == null) {
            return false;
        }
        
        KeyDescriptor updateDescriptor = new KeyDescriptor(oldDescriptor);
        valueStorage.updateKeyDescriptor(updateDescriptor, value);
        
        if (!updateKeyFirst(updateDescriptor)) {
            return false;
        }
        
        try {
            valueStorage.writeValue(updateDescriptor, value);
        } catch (StorageException e) {
            LOG.error(MessageFormat.format("writing object for key {0} failed, attempt to revert", key), e);
            try {
                keyStorage.update(oldDescriptor);
            } catch (StorageException ue) {
                LOG.error(MessageFormat.format("revert of key {0} failed", key), ue);
            }
            throw e;
        }
        return true;
    }
    
    private boolean updateKeyFirst(KeyDescriptor updatedDescriptor) throws StorageException {
        try {
            return keyStorage.update(updatedDescriptor);
        } catch (StorageException e) {
            LOG.error(MessageFormat.format("failed to update key {0}", updatedDescriptor.getKey()), e);
            throw e;
        }
    }

    @Override
    public boolean delete(String key) throws StorageException {
        checkKeyIsNotEmpty(key);
        try {
           return keyStorage.delete(key);
        } catch (StorageException e) {
            LOG.error(MessageFormat.format("removing of key {0} failed", key), e);
            throw e;
        }
    }

    @Override
    public void close() throws IOException {
        Arrays.asList(keyStorage, valueStorage).stream()
            .forEach(this::closeQuiet);
    }
    
    private void closeQuiet(Closeable c) {
        try {
            c.close();
        } catch (Exception e) {
            LOG.error(e);
        }
    }

    public KeyStorage getKeyStorage() {
        return keyStorage;
    }

    public void setKeyStorage(KeyStorage keyStorage) {
        this.keyStorage = keyStorage;
    }

    public ValueStorage getValueStorage() {
        return valueStorage;
    }

    public void setValueStorage(ValueStorage valueStorage) {
        this.valueStorage = valueStorage;
    }
    
}
