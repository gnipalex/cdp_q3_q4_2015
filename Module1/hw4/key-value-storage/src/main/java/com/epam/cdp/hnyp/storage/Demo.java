package com.epam.cdp.hnyp.storage;

import java.io.IOException;

import com.epam.cdp.hnyp.storage.exception.StorageException;
import com.epam.cdp.hnyp.storage.factory.KeyValueStorageFactory;


public class Demo {
    public static void main(String[] args) throws IOException, StorageException {
        testKeyValueStorage();
    }
    
    public static void testKeyValueStorage() throws IOException, StorageException {
        try (KeyValueStorage storage = KeyValueStorageFactory.createDefaultKeyValueStorage()) {
            storage.create("key2", new Integer(15));
            System.out.println(storage.read("key1"));
            
            storage.create("string", "here is some string");
            System.out.println(storage.read("string")); 
            storage.update("string", "this is update value, the text is long, this is update value, the text is long enough");
            System.out.println(storage.read("string")); 
            
            //storage.create("null", null);
        }
    }
}
