package com.epam.cdp.hnyp.storage.value;

import java.io.Closeable;

import com.epam.cdp.hnyp.storage.exception.StorageException;
import com.epam.cdp.hnyp.storage.key.KeyDescriptor;

public interface ValueStorage extends Closeable {
    Object readValue(KeyDescriptor keyDescriptor) throws StorageException;
    void writeValue(KeyDescriptor keyDescriptor, Object value) throws StorageException;
    int calculateLength(Object value);
    void updateKeyDescriptor(KeyDescriptor keyDescriptor, Object value);
}
