package com.epam.cdp.hnyp.storage.key;

import java.text.MessageFormat;

import org.apache.commons.lang3.StringUtils;

public class KeyDescriptor {
    private String key;
    private int blocksCount;
    private int valueLength;
    private int startBlock;
    private boolean deleted;
    private String className;
    
    public KeyDescriptor(String key, int valueLength, int blocksCount,
            int startBlock, Class<?> clazz) {
        this.key = key;
        this.blocksCount = blocksCount;
        this.valueLength = valueLength;
        this.startBlock = startBlock;
        this.className = clazz.getCanonicalName();
    }
    
    public KeyDescriptor(KeyDescriptor source) {
        this.blocksCount = source.blocksCount;
        this.deleted = source.deleted;
        this.key = source.key;
        this.startBlock = source.startBlock;
        this.valueLength = source.valueLength;
        this.className = source.className;
    }

    public static KeyDescriptor createAfterLast(KeyDescriptor lastDescriptor,
            String key, int valueLength, int blockSize, Class<?> clazz) {
        int requiredBlockCount = requiredBlocksCount(valueLength, blockSize);
        int startBlock = lastDescriptor.startBlock + lastDescriptor.blocksCount;
        return new KeyDescriptor(key, valueLength, requiredBlockCount,  startBlock, clazz);
    }
    
    public static int requiredBlocksCount(int valueLength, int blockSize) {
        int requiredBlocksCount = valueLength / blockSize;
        if (valueLength % blockSize > 0) {
            requiredBlocksCount++;
        }
        return requiredBlocksCount;
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

    public void markDeleted() {
        this.deleted = true;
    }
    
    public void resetDeleted() {
        this.deleted = false;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setBlocksCount(int blocksCount) {
        this.blocksCount = blocksCount;
    }

    public void setStartBlock(int startBlock) {
        this.startBlock = startBlock;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
    
    public void setValueClass(Class<?> clazz) {
        this.className = clazz.getCanonicalName();
    }
    
    public Class<?> getValueClass() throws ClassNotFoundException {
        if (StringUtils.isNotBlank(this.className)) {
            return Class.forName(className);
        }
        return null;
    }

    @Override
    public String toString() {
        return MessageFormat.format("[key = {0}, blocksCount = {1}, "
                + "valueLength = {2}, startBlock = {3}, deleted = {4}, clazz = {5}]",
                this.key, this.blocksCount, this.valueLength, this.startBlock,
                this.deleted, this.className);
    }

}
