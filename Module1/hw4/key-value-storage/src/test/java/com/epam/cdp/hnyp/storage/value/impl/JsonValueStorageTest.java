package com.epam.cdp.hnyp.storage.value.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.epam.cdp.hnyp.storage.exception.StorageException;
import com.epam.cdp.hnyp.storage.key.KeyDescriptor;
import com.epam.cdp.hnyp.storage.value.block.BlockStorage;
import com.google.gson.Gson;

@RunWith(MockitoJUnitRunner.class)
public class JsonValueStorageTest {

    private final static String DEFAULT_ENCODING = "cp1251";
    
    private final static int DEFAULT_BLOCK_SIZE = 100;
    
    @Mock
    private BlockStorage mockBlockStorage;

    @InjectMocks
    private JsonValueStorage jsonValueStorage = new JsonValueStorage(mockBlockStorage, DEFAULT_BLOCK_SIZE, DEFAULT_ENCODING);;
    
    @Test(expected=IllegalArgumentException.class)
    public void shouldThrowException_whenEmptyEncodingSpecified() {
        new JsonValueStorage(mockBlockStorage, DEFAULT_BLOCK_SIZE, StringUtils.EMPTY);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void shouldThrowException_whenZeroBlockSizeSpecified() {
        new JsonValueStorage(mockBlockStorage, 0, DEFAULT_ENCODING);
    }
    
    @Test
    public void shouldReadTwoBlocks_whenReadValueLengthIsMoreThanOneButLessThanTwoBlocksInSize() throws StorageException, UnsupportedEncodingException {
        KeyDescriptor keyDescriptor = new KeyDescriptor("aa", 101, 3, 0, Integer.class);
        String integerValueString = "22";
        when(mockBlockStorage.readBlock(0)).thenReturn(fillBlockWithValue(integerValueString));
        when(mockBlockStorage.readBlock(1)).thenReturn(fillBlockWithValue(""));
        jsonValueStorage.readValue(keyDescriptor);
        
        verify(mockBlockStorage).readBlock(0);
        verify(mockBlockStorage).readBlock(1);
    }
    
    private byte[] fillBlockWithValue(String value) throws UnsupportedEncodingException {
        return fillBlockWithValue(value, DEFAULT_BLOCK_SIZE);
    }
    
    private byte[] fillBlockWithValue(String value, int blockSize) throws UnsupportedEncodingException {
        byte[] block = new byte[blockSize];
        byte[] valueBytes = value.getBytes(DEFAULT_ENCODING);
        System.arraycopy(valueBytes, 0, block, 0, valueBytes.length);
        return block;
    }
    
    private int getActualLengthForValue(Object value) {
        Gson gson = new Gson();
        return gson.toJson(value).length();
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void shouldThrowException_whenReadWithNullDescriptor() throws StorageException {
        jsonValueStorage.readValue(null);
    }
    
    @Test(expected=StorageException.class)
    public void shouldThrowException_whenReadingWithInvalidDescriptor() throws StorageException {
        KeyDescriptor keyDescriptor = new KeyDescriptor(null, 1, 1, 1, Object.class);
        jsonValueStorage.readValue(keyDescriptor);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void shouldThrowException_whenWritingNullValue() throws StorageException {
        KeyDescriptor keyDescriptor = new KeyDescriptor("aa", 1, 1, 1, Object.class);
        jsonValueStorage.writeValue(keyDescriptor, null);
    }
    
    @Test
    public void shouldWriteTwoBlocks_whenValueLengthIsMoreThanOneBlockButLessThanTwo() throws StorageException, UnsupportedEncodingException {
        int blockSize = 10;
        jsonValueStorage = new JsonValueStorage(mockBlockStorage, blockSize, DEFAULT_ENCODING);
        String valueToWrite = "01234567890";
        KeyDescriptor keyDescriptor = new KeyDescriptor("aa", valueToWrite.length(), 1, 0, String.class);
        jsonValueStorage.writeValue(keyDescriptor, valueToWrite);

        verify(mockBlockStorage).writeBlock(fillBlockWithValue("\"012345678", blockSize), 0);
        verify(mockBlockStorage).writeBlock(fillBlockWithValue("90\"", blockSize), 1);
    }
    
    @Test
    public void shouldReturnLengthOfEncodedToJsonValue_whenCalculateLengthForValue() {
        String value = "this is string";
        int result = jsonValueStorage.calculateLength(value);
        int actualLength = getActualLengthForValue(value);
        assertEquals(actualLength, result);
    }
    
    @Test
    public void shouldSetActualLengthToDescriptor_whenUpdatingDescriptor() {
        String value = "this is string";
        KeyDescriptor keyDescriptor = new KeyDescriptor("aa", 0, 1, 0, String.class);
        
        jsonValueStorage.updateKeyDescriptor(keyDescriptor, value);
        
        int actualLength = getActualLengthForValue(value);
        assertEquals(actualLength, keyDescriptor.getValueLength());
    }
    
    @Test
    public void shouldSetRequiredBlocksCount_whenUpdatingDescriptor() {
        String value = "this is string";
        KeyDescriptor keyDescriptor = new KeyDescriptor("aa", 0, 1, 0, String.class);
        
        jsonValueStorage.updateKeyDescriptor(keyDescriptor, value);
        
        int actualRequiredBlocks = 1;
        assertEquals(actualRequiredBlocks, keyDescriptor.getBlocksCount());
    }
    
    @Test
    public void shouldWriteBlockAtStartBlockPosition_whenWritingFirstBlock() throws StorageException {
        int startBlock = 5;
        KeyDescriptor keyDescriptor = new KeyDescriptor("aaa", 10, 1, startBlock, Object.class);
        jsonValueStorage.writeValue(keyDescriptor, new Object());
        
        verify(mockBlockStorage).writeBlock(any(), eq(startBlock));
    }
    

}
