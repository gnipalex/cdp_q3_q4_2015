package com.epam.cdp.hnyp.storage.impl;

import java.text.MessageFormat;

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
 
    public DefaultKeyValueStorage(KeyStorage keyStorage, ValueStorage valueStorage) {
        this.keyStorage = keyStorage;
        this.valueStorage = valueStorage;
    }
    
    @Override
    public boolean create(String key, Object value) {
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
                keyStorage.delete(key);
                LOG.error(MessageFormat.format("value writing failed, deleting key '{0}'", key), e);
                return false;
            }
        } catch (StorageException e) {
            LOG.error(MessageFormat.format("failed to store key '{0}'", key), e);
            return false;
        }
        return true;
    }

    @Override
    public Object read(String key) {
        KeyDescriptor keyDescriptor = keyStorage.read(key);
        if (keyDescriptor == null) {
            return null;
        }
        try {
            return valueStorage.readValue(keyDescriptor);
        } catch (StorageException e) {
            LOG.error(MessageFormat.format("error while reading key '{0}'", key), e);
        }
        return null;
    }

    @Override
    public boolean update(String key, Object value) {
        KeyDescriptor oldDescriptor = keyStorage.read(key);
        KeyDescriptor updateDescriptor = new KeyDescriptor(oldDescriptor);
        valueStorage.updateKeyDescriptor(updateDescriptor, value);
        try {
            if (!keyStorage.update(updateDescriptor)) {
                return false;
            }
        } catch (StorageException e) {
            LOG.error(MessageFormat.format("failed to update key '{0}'", key), e);
            return false;
        }
        try {
            valueStorage.writeValue(updateDescriptor, value);
            return true;
        } catch (StorageException e) {
            LOG.error(MessageFormat.format("writing object for key '{0}' failed, attempt to revert", key), e);
            try {
                keyStorage.update(oldDescriptor);
            } catch (StorageException ue) {
                LOG.error(MessageFormat.format("revert of key '{0}' failed", key), ue);
            }
        }
        return false;
    }

    @Override
    public boolean delete(String key) {
        try {
           return keyStorage.delete(key);
        } catch (StorageException e) {
            LOG.error(MessageFormat.format("removing of key '{0}' failed", key), e);
        }
        return false;
    }

}
