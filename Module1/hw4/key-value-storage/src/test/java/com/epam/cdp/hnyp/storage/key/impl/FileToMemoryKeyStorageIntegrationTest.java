package com.epam.cdp.hnyp.storage.key.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import com.epam.cdp.hnyp.storage.exception.StorageException;
import com.epam.cdp.hnyp.storage.key.KeyDescriptor;
import com.epam.cdp.hnyp.storage.key.KeyStorage;
import com.google.gson.Gson;

public class FileToMemoryKeyStorageIntegrationTest {

    private static final int BLOCK_SIZE = 100; 
    
    private static final String ENCODING = "cp1251";
    
    private static final File KEY_FILE = new File("test/keyfile");
    
    private static final String DEFAULT_KEY = "mykey";
    private static final int DEFAULT_VALUE_LENGTH = 10;
    private static final Class<?> DEFAULT_CLASS = Object.class;
    
    @Before
    public void deleteFileWithKeys() {
        KEY_FILE.delete();
    }
    
    @AfterClass
    public static void deleteFileAfterTests() {
        KEY_FILE.delete();
    }
    
    private FileToMemoryKeyStorage createKeyStorage() throws IOException, StorageException {
        return new FileToMemoryKeyStorage(KEY_FILE, ENCODING, BLOCK_SIZE);
    }
    
    private KeyDescriptor createFirstDefaultDescriptor(KeyStorage keyStorage) throws StorageException {
        return keyStorage.create(DEFAULT_KEY, DEFAULT_VALUE_LENGTH, DEFAULT_CLASS);
    }
    
    private KeyDescriptor createDescriptor(KeyStorage keyStorage, String key, int valueLength, Class<?> clazz) throws StorageException {
        return keyStorage.create(key, valueLength, clazz);
    }
    
    @Test
    public void shouldSaveEmptyFileOnClose_whenNoKeysAdded() throws IOException, StorageException {
        FileToMemoryKeyStorage keyStorage = createKeyStorage();
        keyStorage.close();
        assertTrue(KEY_FILE.exists());
        assertTrue(KEY_FILE.length() == 0);
    }
    
    @Test
    public void shouldNotFail_whenEmptyKeyFileSpecified() throws IOException, StorageException {
        try (FileToMemoryKeyStorage keyStorage = createKeyStorage()) {  
        }
    }
    
    @Test
    public void shouldSaveKeyToFile_whenCreatingNewKey() throws IOException, StorageException, ClassNotFoundException { 
        try (FileToMemoryKeyStorage keyStorage = createKeyStorage()) {
            KeyDescriptor createdDescriptor = createFirstDefaultDescriptor(keyStorage);
            assertNotNull(createdDescriptor);
            
            List<String> Lines = readAllLines(KEY_FILE);
            KeyDescriptor writtenDescriptor = parseDescriptor(Lines.get(0));
            
            assertEquals(DEFAULT_KEY, writtenDescriptor.getKey());
            assertEquals(DEFAULT_VALUE_LENGTH, writtenDescriptor.getValueLength());
            assertEquals(DEFAULT_CLASS, writtenDescriptor.getValueClass());
        }
    }
    
    @Test
    public void shouldAppendTheSameKey_whenUpdatingKeyAndBlockCountDoesntChanged() throws StorageException, IOException, ClassNotFoundException {
        try (FileToMemoryKeyStorage keyStorage = createKeyStorage()) {
            KeyDescriptor createdDescriptor = createFirstDefaultDescriptor(keyStorage);
            assertNotNull(createdDescriptor);
            
            int newValueLength = 20;
            Class<?> newClazz = String.class;
            KeyDescriptor updateDescriptor = new KeyDescriptor(createdDescriptor);
            updateDescriptor.setValueLength(newValueLength);
            updateDescriptor.setValueClass(newClazz);
            boolean successUpdated = keyStorage.update(updateDescriptor);
            assertTrue(successUpdated);
            
            List<String> Lines = readAllLines(KEY_FILE);
            KeyDescriptor writtenDescriptor = parseDescriptor(Lines.get(0));
            KeyDescriptor writtenUpdatedDescriptor = parseDescriptor(Lines.get(1));
            
            assertEquals(writtenDescriptor.getKey(), writtenUpdatedDescriptor.getKey());
            assertEquals(writtenDescriptor.getBlocksCount(), writtenUpdatedDescriptor.getBlocksCount());
            assertEquals(writtenDescriptor.getStartBlock(), writtenUpdatedDescriptor.getStartBlock());
            assertEquals(newValueLength, writtenUpdatedDescriptor.getValueLength());
            assertEquals(newClazz, writtenUpdatedDescriptor.getValueClass());
        } 
    }
    
    @Test
    public void shouldAppendTheSameKeyAsDeletedAndAgainUpdatedKey_whenUpdateKeyAndBlockCountGrew() throws StorageException, IOException {
        try (FileToMemoryKeyStorage keyStorage = createKeyStorage()) {
            KeyDescriptor createdDescriptor = createFirstDefaultDescriptor(keyStorage);
            assertNotNull(createdDescriptor);
        
            int newValueLength = BLOCK_SIZE * 2;
            KeyDescriptor resizedDescriptor = KeyDescriptor.resize(createdDescriptor, newValueLength, BLOCK_SIZE);
            boolean succesfulyUpdated = keyStorage.update(resizedDescriptor);
            assertTrue(succesfulyUpdated);
            
            List<String> lines = readAllLines(KEY_FILE);
            KeyDescriptor writtenDescriptor = parseDescriptor(lines.get(0));
            KeyDescriptor writtenAsDeletedDescriptor = parseDescriptor(lines.get(1));
            KeyDescriptor writtenAsNewDescriptor = parseDescriptor(lines.get(2));
            assertNotEquals(writtenDescriptor.getKey(), writtenAsDeletedDescriptor.getKey());
            assertEquals(writtenDescriptor.getKey(), writtenAsNewDescriptor.getKey());
        }
    }
    
    @Test
    public void shouldAppendTheSameKeyAsDeleted_whenDeletingKey() throws IOException, StorageException {
        try (FileToMemoryKeyStorage keyStorage = createKeyStorage()) {
            KeyDescriptor createdDescriptor = createFirstDefaultDescriptor(keyStorage);
            assertNotNull(createdDescriptor);
            
            boolean successfulyDeleted = keyStorage.delete(DEFAULT_KEY);
            assertTrue(successfulyDeleted);
            
            List<String> lines = readAllLines(KEY_FILE);
            KeyDescriptor writtenDescriptor = parseDescriptor(lines.get(0));
            KeyDescriptor writtenAsDeletedDescriptor = parseDescriptor(lines.get(1));
            assertEquals(writtenDescriptor.getKey(), writtenAsDeletedDescriptor.getKey());
            assertTrue(writtenAsDeletedDescriptor.isDeleted());
        }
    }
    
    @Test
    public void shouldReadAllKeys_whenCreatingInstance() throws IOException, StorageException {
        KeyDescriptor createdDescriptor1 = null, createdDescriptor2 = null;
        String secondKey = "secondkey";
        int secondValueLength = 36;
        Class<?> secondClazz = String.class;
        
        try (FileToMemoryKeyStorage keyStorage = createKeyStorage()) {
            createdDescriptor1 = createFirstDefaultDescriptor(keyStorage);
            assertNotNull(createdDescriptor1);
            createdDescriptor2 = createDescriptor(keyStorage, secondKey, secondValueLength, secondClazz);
            assertNotNull(createdDescriptor2);
        }
        //now reopening keyStorage, all created values should be read
        try (FileToMemoryKeyStorage keyStorage = createKeyStorage()) {
            KeyDescriptor readedDescriptor1 = keyStorage.read(DEFAULT_KEY);
            assertNotNull(readedDescriptor1);
            KeyDescriptor readedDescriptor2 = keyStorage.read(secondKey);
            assertNotNull(readedDescriptor2);
        }
    }
    
    @Test
    public void shouldRewriteAllKeys_whenClosing() throws IOException, StorageException {
        KeyDescriptor createdDescriptor1 = null, createdDescriptor2 = null;
        String secondKey = "secondkey";
        int secondValueLength = 36;
        Class<?> secondClazz = String.class;
        try (FileToMemoryKeyStorage keyStorage = createKeyStorage()) {
            createdDescriptor1 = createFirstDefaultDescriptor(keyStorage);
            assertNotNull(createdDescriptor1);
            createdDescriptor2 = createDescriptor(keyStorage, secondKey, secondValueLength, secondClazz);
            assertNotNull(createdDescriptor2);
            
            createdDescriptor2.setValueLength(30);
            keyStorage.update(createdDescriptor2);
            
            List<String> lines = readAllLines(KEY_FILE);
            assertEquals(3, lines.size());
        }
        
        List<String> lines = readAllLines(KEY_FILE);
        assertEquals(2, lines.size());
        
        List<KeyDescriptor> descriptorsAfterClosing = lines.stream()
                .map(line -> parseDescriptor(line))
                .collect(Collectors.toList());
        assertTrue(descriptorsAfterClosing.stream().anyMatch(d -> d.getKey().equals(DEFAULT_KEY)));
        assertTrue(descriptorsAfterClosing.stream().anyMatch(d -> d.getKey().equals(secondKey)));
        
    }
    
    @Test
    public void shouldReturnNull_whenCreatingExistingKey() throws StorageException, IOException {
        try (FileToMemoryKeyStorage keyStorage = createKeyStorage()) {
            KeyDescriptor createdDescriptor = createFirstDefaultDescriptor(keyStorage);
            assertNotNull(createdDescriptor);
            KeyDescriptor createdWithSameKeyDescriptor = keyStorage.create(DEFAULT_KEY, DEFAULT_VALUE_LENGTH, DEFAULT_CLASS);
            assertNull(createdWithSameKeyDescriptor);
        }
    }
    
    @Test
    public void shouldReturnFalse_whenUpdatingNonExistentKey() throws IOException, StorageException {
        try (FileToMemoryKeyStorage keyStorage = createKeyStorage()) {
            KeyDescriptor createdDescriptor = createFirstDefaultDescriptor(keyStorage);
            assertNotNull(createdDescriptor);
            createdDescriptor.setKey("newkey");
            boolean succesfulyUpdated = keyStorage.update(createdDescriptor);
            assertFalse(succesfulyUpdated);
        }
    }
    
    @Test
    public void shouldReturnFalse_whenRemovingNonExistentKey() throws IOException, StorageException {
        try (FileToMemoryKeyStorage keyStorage = createKeyStorage()) {
            boolean succesfulyRemoved = keyStorage.delete(DEFAULT_KEY);
            assertFalse(succesfulyRemoved);
        }
    }
    
    private List<String> readAllLines(File file) throws IOException {
        return Files.readAllLines(
                Paths.get(KEY_FILE.getAbsolutePath()), Charset.forName(ENCODING));
    }
    
    private KeyDescriptor parseDescriptor(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, KeyDescriptor.class);
    }

}
