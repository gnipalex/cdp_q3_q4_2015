package com.epam.cdp.hnyp.storage.value.impl;

import java.nio.charset.Charset;

import org.apache.commons.lang3.StringUtils;

import com.epam.cdp.hnyp.storage.exception.StorageException;
import com.epam.cdp.hnyp.storage.key.KeyDescriptor;
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
        checkEncodingNotEmpty(encoding);
        this.blockStorage = blockStorage;
        this.charset = Charset.forName(encoding);
    }
    
    private void checkEncodingNotEmpty(String encoding) {
        if (StringUtils.isEmpty(encoding)) {
            throw new IllegalArgumentException("encoding shouldn't be empty");
        }
    }
    
    @Override
    public Object readValue(KeyDescriptor keyDescriptor) throws StorageException {
        int startBlock = keyDescriptor.getStartBlock();
        int valueLength = keyDescriptor.getValueLength();
        
        byte[] valueBytes = readValuesBytes(valueLength, startBlock); 
        
        return getObjectFromJsonBytes(valueBytes, keyDescriptor.getClass());
    }
    
    private Object getObjectFromJsonBytes(byte[] jsonBytes, Class<?> type) {
        String json = new String(jsonBytes, charset);
        return gsonBuilder.create().fromJson(json, type);
    }
    
    private byte[] readValuesBytes(int valueLength, int startBlock) throws StorageException {
        byte[] valueBytes = new byte[valueLength];
        int blocksCount = KeyDescriptor.requiredBlocksCount(valueLength, blockSize);
        for (int i=0; i<blocksCount; i++) {
            byte[] blockBytes = blockStorage.readBlock(startBlock + i);
            int position = i * blockSize;
            int valueLengthInReadBlock = valueLength - position;
            System.arraycopy(blockBytes, 0, valueBytes, position, valueLengthInReadBlock);
        }
        return valueBytes;
    }

    @Override
    public void writeValue(KeyDescriptor keyDescriptor, Object value) throws StorageException {
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
        int valueLength = calculateLength(value);
        int requiredBlockCount = KeyDescriptor.requiredBlocksCount(valueLength, blockSize);
        keyDescriptor.setBlocksCount(requiredBlockCount);
        keyDescriptor.setValueLength(valueLength);
    }

}
