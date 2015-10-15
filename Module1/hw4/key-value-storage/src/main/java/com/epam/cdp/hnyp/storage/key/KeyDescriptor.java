package com.epam.cdp.hnyp.storage.key;

import java.text.MessageFormat;

public class KeyDescriptor {
    private String key;
    private int blocksCount;
    private int valueLength;
    private int startBlock;
    private boolean deleted;
    private Class<?> clazz;
    
    public KeyDescriptor(String key, int valueLength, int blocksCount,
            int startBlock, Class<?> clazz) {
        this.key = key;
        this.blocksCount = blocksCount;
        this.valueLength = valueLength;
        this.startBlock = startBlock;
        this.clazz = clazz;
    }
    
    public KeyDescriptor(KeyDescriptor source) {
        this.blocksCount = source.blocksCount;
        this.deleted = source.deleted;
        this.key = source.key;
        this.startBlock = source.startBlock;
        this.valueLength = source.valueLength;
        this.clazz = source.clazz;
    }

    public static KeyDescriptor createAfterLast(KeyDescriptor lastDescriptor,
            String key, int valueLength, int blockSize, Class<?> clazz) {
        int requiredBlockCount = requiredBlocksCount(valueLength, blockSize);
        int startBlock = lastDescriptor.startBlock + lastDescriptor.blocksCount;
        return new KeyDescriptor(key, valueLength, requiredBlockCount,  startBlock, clazz);
    }
    
    private static int requiredBlocksCount(int valueLength, int blockSize) {
        return valueLength / blockSize + 1;
    }
    
    public static KeyDescriptor createFromStart(String key, int valueLength, int blockSize, 
            Class<?> clazz) {
        int requiredBlockCount = requiredBlocksCount(valueLength, blockSize);
        return new KeyDescriptor(key, valueLength, requiredBlockCount, 0, clazz);
    }
    
    public static KeyDescriptor resize(KeyDescriptor original, int newLength, int blockSize) {
        KeyDescriptor resizedDescriptor = new KeyDescriptor(original);
        resizedDescriptor.setValueLength(newLength);
        resizedDescriptor.setBlocksCount(requiredBlocksCount(newLength, blockSize));
        return resizedDescriptor;
    }

    public String getKey() {
        return key;
    }

    public int getBlocksCount() {
        return blocksCount;
    }

    public int getValueLength() {
        return valueLength;
    }

    public void setValueLength(int valueLength) {
        this.valueLength = valueLength;
    }

    public int getStartBlock() {
        return startBlock;
    }

    public boolean isDeleted() {
        return deleted;
    }

    void markDeleted() {
        this.deleted = true;
    }
    
    void resetDeleted() {
        this.deleted = false;
    }

    void setKey(String key) {
        this.key = key;
    }

    void setBlocksCount(int blocksCount) {
        this.blocksCount = blocksCount;
    }

    void setStartBlock(int startBlock) {
        this.startBlock = startBlock;
    }

    void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String toString() {
        return MessageFormat.format("[key = {0}, blocksCount = {1}, "
                + "valueLength = {2}, startBlock = {3}, deleted = {4}, clazz = {5}]",
                this.key, this.blocksCount, this.valueLength, this.startBlock,
                this.deleted, this.clazz);
    }

}
