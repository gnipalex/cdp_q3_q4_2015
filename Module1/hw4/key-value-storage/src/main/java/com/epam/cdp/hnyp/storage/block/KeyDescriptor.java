package com.epam.cdp.hnyp.storage.block;

public class KeyDescriptor {
    private String key;
    private int blocksCount;
    private int length;
    private int startBlock;
    private boolean deleted;
    
    private KeyDescriptor() {
    }
    
    public KeyDescriptor(String key, int blocksCount, int length, int startBlock) {
        super();
        this.key = key;
        this.blocksCount = blocksCount;
        this.length = length;
        this.startBlock = startBlock;
    }

    public static KeyDescriptor createAfterLast(KeyDescriptor lastDescriptor, int length, int blockSz, String key) {
        // TODO add argument check
        KeyDescriptor keyDescriptor = new KeyDescriptor();
        int requiredBlockCount = length / blockSz + 1;
        int startBlock = lastDescriptor.startBlock + lastDescriptor.blocksCount;
        keyDescriptor.key = key;
        keyDescriptor.blocksCount = requiredBlockCount;
        keyDescriptor.deleted = false;
        keyDescriptor.length = length;
        keyDescriptor.startBlock = startBlock;
        return keyDescriptor;
    }

    public String getKey() {
        return key;
    }

    public int getBlocksCount() {
        return blocksCount;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
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

}
