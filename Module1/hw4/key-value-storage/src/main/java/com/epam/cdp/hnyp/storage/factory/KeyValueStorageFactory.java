package com.epam.cdp.hnyp.storage.factory;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.epam.cdp.hnyp.storage.KeyValueStorage;
import com.epam.cdp.hnyp.storage.exception.StorageException;
import com.epam.cdp.hnyp.storage.impl.DefaultKeyValueStorage;
import com.epam.cdp.hnyp.storage.key.KeyStorage;
import com.epam.cdp.hnyp.storage.key.impl.FileToMemoryKeyStorage;
import com.epam.cdp.hnyp.storage.value.ValueStorage;
import com.epam.cdp.hnyp.storage.value.block.BlockStorage;
import com.epam.cdp.hnyp.storage.value.block.impl.FileMappingBlockStorage;
import com.epam.cdp.hnyp.storage.value.impl.JsonValueStorage;

public class KeyValueStorageFactory {
    
    private static final Logger LOG = Logger.getLogger(KeyValueStorageFactory.class);
    
    private static final String DEFAULT_ENCODING = "cp1251";
    private static final int DEFAULT_BLOCK_SIZE = 64;
    private static final File DEFAULT_KEYS_FILE = new File("storage.keys");
    private static final File DEFAULT_VALUES_FILE = new File("storage.values");
    
    public static KeyValueStorage createDefaultKeyValueStorage() {
        try {
            KeyStorage keyStorage = new FileToMemoryKeyStorage(DEFAULT_KEYS_FILE, DEFAULT_ENCODING, DEFAULT_BLOCK_SIZE);
        
            BlockStorage blockStorage = new FileMappingBlockStorage(DEFAULT_VALUES_FILE, DEFAULT_BLOCK_SIZE); 
            ValueStorage valueStorage = new JsonValueStorage(blockStorage, DEFAULT_BLOCK_SIZE, DEFAULT_ENCODING);
            
            KeyValueStorage keyValueStorage = new DefaultKeyValueStorage(keyStorage, valueStorage);
            return keyValueStorage; 
        } catch (StorageException | IOException e) {
            LOG.error("keyvalue storage creation failed", e);
            return null;
        } 
    }
}
