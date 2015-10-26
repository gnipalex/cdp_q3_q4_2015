package com.epam.cdp.hnyp.storage.value.impl;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.epam.cdp.hnyp.storage.exception.InvalidDescriptorException;
import com.epam.cdp.hnyp.storage.exception.StorageException;
import com.epam.cdp.hnyp.storage.key.KeyDescriptor;
import com.epam.cdp.hnyp.storage.key.KeyDescriptorValidator;
import com.epam.cdp.hnyp.storage.value.ValueStorage;
import com.epam.cdp.hnyp.storage.value.block.BlockStorage;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;

public class JsonValueStorage implements ValueStorage {

    private BlockStorage blockStorage; 
    
    private int blockSize;
    
    private Charset charset;
    
    private GsonBuilder gsonBuilder;
    
    private JsonValueStorage() {
        this.gsonBuilder = new GsonBuilder();
    }
    
    public JsonValueStorage(BlockStorage blockStorage, int blockSize, String encoding) {
        this();
        checkEncodingIsNotEmpty(encoding);
        checkBlockSize(blockSize);
        this.blockStorage = blockStorage;
        this.charset = Charset.forName(encoding);
        this.blockSize = blockSize;
    }
    
    private void checkEncodingIsNotEmpty(String encoding) {
        if (StringUtils.isEmpty(encoding)) {
            throw new IllegalArgumentException("encoding shouldn't be empty");
        }
    }
    
    private void checkBlockSize(int blockSize) {
        if (blockSize <= 0) {
            throw new IllegalArgumentException("block size should be greater than zero");
        }
    }
    
    private void checkValueIsNotNull(Object value) {
        if (value == null) {
            throw new IllegalArgumentException("value should not be null");
        }
    }
    
    @Override
    public Object readValue(KeyDescriptor keyDescriptor) throws StorageException {
        checkDescriptorIsValid(keyDescriptor);
        int startBlock = keyDescriptor.getStartBlock();
        int valueLength = keyDescriptor.getValueLength();
        
        byte[] valueBytes = readValuesBytes(valueLength, startBlock); 
        
        return getObjectFromJsonBytes(valueBytes, keyDescriptor.getValueClass());
    }
    
    private void checkDescriptorIsValid(KeyDescriptor keyDescriptor) throws StorageException {
        if (keyDescriptor == null) {
            throw new IllegalArgumentException("descriptor shouldn't be null");
        }
        List<String> errors = KeyDescriptorValidator.validate(keyDescriptor);
        if (CollectionUtils.isNotEmpty(errors)) {
            String errorMessage = errors.stream().collect(Collectors.joining("; "));
            throw new StorageException("bad descriptor", 
                    new InvalidDescriptorException(MessageFormat.format("{0} - {1}", keyDescriptor, errorMessage)));
            
        }
    }
    
    private Object getObjectFromJsonBytes(byte[] jsonBytes, Class<?> type) {
        String json = new String(jsonBytes, charset).trim();
        return gsonBuilder.create().fromJson(json, type);
    }
    
    private byte[] readValuesBytes(int valueLength, int startBlock) throws StorageException {
        byte[] valueBytes = new byte[valueLength];
        int blocksCount = KeyDescriptor.requiredBlocksCount(valueLength, blockSize);
        for (int i=0; i<blocksCount; i++) {
            byte[] blockBytes = blockStorage.readBlock(startBlock + i);
            int position = i * blockSize;
            int restLength = valueLength - position;
            int valueLengthInReadBlock = restLength > blockSize ? blockSize : restLength;
            System.arraycopy(blockBytes, 0, valueBytes, position, valueLengthInReadBlock);
        }
        return valueBytes;
    }

    @Override
    public void writeValue(KeyDescriptor keyDescriptor, Object value) throws StorageException {
        checkDescriptorIsValid(keyDescriptor);
        checkValueIsNotNull(value);
        int startBlock = keyDescriptor.getStartBlock();
        byte[] valueBytes = getJsonAsBytes(value);
        writeValueBytes(valueBytes, startBlock);
    }
    
    private void writeValueBytes(byte[] valueBytes, int startBlock) throws StorageException {
        int blockCount = KeyDescriptor.requiredBlocksCount(valueBytes.length, blockSize);
        for (int i=0; i<blockCount; i++) {
            byte[] blockBytes = getBlock(valueBytes, i);
            blockStorage.writeBlock(blockBytes, startBlock + i);
        }
    }
    
    private byte[] getBlock(byte[] allValueBytes, int block) {
        byte[] blockBytes = new byte[blockSize];
        int offset = block * blockSize;
        int restLength = allValueBytes.length - offset;
        int lengthToCopy = restLength > blockSize ? blockSize : restLength;
        System.arraycopy(allValueBytes, offset, blockBytes, 0, lengthToCopy);
        return blockBytes;
    }

    @Override
    public int calculateLength(Object value) {
        checkValueIsNotNull(value);
        return getJsonAsBytes(value).length;
    }
    
    private byte[] getJsonAsBytes(Object value) {
        String jsonValue = gsonBuilder.create().toJson(value);
        return jsonValue.getBytes(charset);
    }
    
    public <T> void registerTypeAdapter(TypeAdapter<T> typeAdapter, Class<T> clazz) {
        gsonBuilder.registerTypeAdapter(clazz, typeAdapter);
    }

    @Override
    public void updateKeyDescriptor(KeyDescriptor keyDescriptor,
            Object value) {
        checkValueIsNotNull(keyDescriptor);
        checkValueIsNotNull(value);
        int valueLength = calculateLength(value);
        int requiredBlockCount = KeyDescriptor.requiredBlocksCount(valueLength, blockSize);
        keyDescriptor.setBlocksCount(requiredBlockCount);
        keyDescriptor.setValueLength(valueLength);
    }

    @Override
    public void close() throws IOException {
        blockStorage.close();
    }

}
