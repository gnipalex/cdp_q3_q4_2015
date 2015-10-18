package com.epam.cdp.hnyp.storage.impl;

import com.epam.cdp.hnyp.storage.KeyValueStorage;
import com.epam.cdp.hnyp.storage.block.BlockStorage;
import com.epam.cdp.hnyp.storage.key.KeyStorage;

public class FileKeyValueStorage implements KeyValueStorage {

    public static final int DEFAULT_BLOCK_SIZE = 64;
    
    private BlockStorage blockStorage; 
    private KeyStorage keyStorage;
    private int blockSize;
    
    public FileKeyValueStorage(KeyStorage keyStorage, BlockStorage blockStorage) {
        this.keyStorage = keyStorage;
        this.blockStorage = blockStorage;
        this.blockSize = DEFAULT_BLOCK_SIZE;
    }
    
    public FileKeyValueStorage(KeyStorage keyStorage, BlockStorage blockStorage, int blockSize) {
        this(keyStorage, blockStorage);
        this.blockSize = blockSize;
    }
    
    @Override
    public boolean create(String key, Object value) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Object read(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean update(String key, Object value) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean delete(String key) {
        // TODO Auto-generated method stub
        return false;
    }

}
