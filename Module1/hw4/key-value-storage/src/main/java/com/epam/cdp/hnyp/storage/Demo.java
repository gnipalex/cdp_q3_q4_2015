package com.epam.cdp.hnyp.storage;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Random;

import com.epam.cdp.hnyp.storage.exception.StorageException;
import com.epam.cdp.hnyp.storage.factory.KeyValueStorageFactory;


public class Demo {
    public static void main(String[] args) throws IOException, StorageException {
        //testKeyValueStorage();
        long start = System.currentTimeMillis();
        File folder = new File("tmp/manyElements");
        //fillManyElements(folder, 1000_000);
        openWithManyKeys(folder);
        long finish = System.currentTimeMillis();
        System.out.println("it took " + (finish - start));
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
    
    public static void fillManyElements(File folder, int howMany) throws IOException, StorageException {
        
        try (KeyValueStorage storage = KeyValueStorageFactory.createDefaultKeyValueStorage(folder)) {
            for (int i=0; i<howMany; i++) {
                String key = "key" + i;
                String[] values = generateStringsArray();
                if (!storage.create(key, values)) {
                    System.out.println(MessageFormat.format("key {0} already presents", key));
                }
            }
        } 
    }
    
    public static void openWithManyKeys(File folder) throws IOException {
        try (KeyValueStorage storage = KeyValueStorageFactory.createDefaultKeyValueStorage(folder)) {
            
        }
    }
    
    private static String[] generateStringsArray() {
        Random rand = new Random();
        int size = rand.nextInt(10) + 5;
        String[] strings = new String[size];
        for (int i=0; i<size; i++) {
            strings[i] = String.valueOf(rand.nextInt(99999999));
        }
        return strings;
    }
    
    
}
