package com.epam.cdp.hnyp.storage.key.impl;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.epam.cdp.hnyp.storage.exception.StorageException;
import com.epam.cdp.hnyp.storage.key.KeyDescriptor;
import com.google.gson.Gson;

public class FileToMemoryKeyStorageIntegrationTest {

    private static final int BLOCK_SIZE = 100; 
    
    private static final File KEY_FILE = new File("test/keyfile");
    
    @Before
    public void deleteFileWithKeys() {
        KEY_FILE.delete();
    }
    
    @Test
    public void shouldSaveEmptyFileOnClose_whenNoKeysAdded() throws IOException, StorageException {
        FileToMemoryKeyStorage keyStorage = new FileToMemoryKeyStorage(KEY_FILE, BLOCK_SIZE);
        keyStorage.close();
        assertTrue(KEY_FILE.exists());
        assertTrue(KEY_FILE.length() == 0);
    }
    
    @Test
    public void shouldNotFail_whenEmptyKeyFileSpecified() throws IOException, StorageException {
        try (FileToMemoryKeyStorage keyStorage = new FileToMemoryKeyStorage(KEY_FILE, BLOCK_SIZE)) {  
        }
    }
    
    @Test
    public void shouldSaveKeyToFile_whenCreatingNewKey() throws IOException, StorageException, ClassNotFoundException { 
        try (FileToMemoryKeyStorage keyStorage = new FileToMemoryKeyStorage(KEY_FILE, BLOCK_SIZE)) {
            String key = "mykey";
            int valueLength = 10;
            Class<?> clazz = Object.class;
            KeyDescriptor createdDescriptor = keyStorage.create(key, valueLength, clazz);
            assertNotNull(createdDescriptor);
            
            List<String> Lines = readAllLines(KEY_FILE);
            KeyDescriptor writtenDescriptor = parseDescriptor(Lines.get(0));
            
            assertEquals(key, writtenDescriptor.getKey());
            assertEquals(valueLength, writtenDescriptor.getValueLength());
            assertEquals(clazz, writtenDescriptor.getValueClass());
        }
    }
    
    @Test
    public void shouldAppendTheSameKey_whenUpdatingKeyAndBlockCountDoesntChanged() throws StorageException, IOException, ClassNotFoundException {
        try (FileToMemoryKeyStorage keyStorage = new FileToMemoryKeyStorage(KEY_FILE, BLOCK_SIZE)) {
            String key = "mykey";
            int valueLength = 10;
            Class<?> clazz = Object.class;
            KeyDescriptor createdDescriptor = keyStorage.create(key, valueLength, clazz);
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
        try (FileToMemoryKeyStorage keyStorage = new FileToMemoryKeyStorage(KEY_FILE, BLOCK_SIZE)) {
            String key = "mykey";
            int valueLength = 10;
            Class<?> clazz = Object.class;
            KeyDescriptor createdDescriptor = keyStorage.create(key, valueLength, clazz);
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
    public void shouldAppendTheSameKeyAsDeleted_whenDeletingKey() {
        
    }
    
    @Test
    public void shouldReadAllKeys_whenCreatingInstance() {
        
    }
    
    @Test
    public void shouldAppendNewKey_whenUpdateAndBlockCountGrew() {
        
    }
    
    @Test
    public void shouldRewriteAllKeys_whenClosing() {
        
    }
    
    @Test
    public void shouldReturnNull_whenCreatingExistingKey() {
        
    }
    
    @Test
    public void shouldReturnFalse_whenUpdatingNonExistentKey() {
        
    }
    
    @Test
    public void shouldReturnFalse_whenRemovingNonExistentKey() {
        
    }
    
    private List<String> readAllLines(File file) throws IOException {
        return Files.readAllLines(Paths.get(KEY_FILE.getAbsolutePath()), Charset.forName("cp1251"));
    }
    
    private KeyDescriptor parseDescriptor(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, KeyDescriptor.class);
    }

    @BeforeClass
    public static void beforeIntegrationTests() {
        
    }

}
