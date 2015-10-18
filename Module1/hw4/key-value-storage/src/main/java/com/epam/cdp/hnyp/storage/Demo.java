package com.epam.cdp.hnyp.storage;

import java.io.IOException;

import com.epam.cdp.hnyp.storage.factory.KeyValueStorageFactory;


public class Demo {
    public static void main(String[] args) throws IOException {
        testKeyValueStorage();
    }
    
    public static void testKeyValueStorage() throws IOException {
        try (KeyValueStorage storage = KeyValueStorageFactory.createDefaultKeyValueStorage()) {
            storage.create("key1", new Integer(15));
            System.out.println(storage.read("key1"));
            
            storage.create("string", "here is some string");
            System.out.println(storage.read("string"));    
        }
    }
}
