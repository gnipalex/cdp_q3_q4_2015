package com.epam.cdp.hnyp.storage.value.block;

import java.io.Closeable;

import com.epam.cdp.hnyp.storage.exception.StorageException;

public interface BlockStorage extends Closeable {
    byte[] readBlock(int offset) throws StorageException;
    void writeBlock(byte[] source, int offset) throws StorageException;
}