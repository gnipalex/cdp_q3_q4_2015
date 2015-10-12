package com.epam.cdp.hnyp.storage.block;

import com.epam.cdp.hnyp.storage.block.exception.StorageException;

public interface BlockStorage {
    byte[] readBlock(int offset) throws StorageException;
    void writeBlock(byte[] source, int offset) throws StorageException;
}