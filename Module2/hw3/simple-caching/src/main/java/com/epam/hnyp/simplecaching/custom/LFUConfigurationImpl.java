package com.epam.hnyp.simplecaching.custom;

import java.text.MessageFormat;


public class LFUConfigurationImpl<K, V> implements LFUConfiguration<K, V> {

    private static final long serialVersionUID = 1L;
    
    private int maxSize;
    private float evictionFactor;
    private int maxFrequency;

    public LFUConfigurationImpl(int maxSize, float evictionFactor, int maxFrequency) {
        checkEvictionFactor(evictionFactor);
        checkValueIsPositive(maxSize, MessageFormat.format("cache max size {0} should > 0", maxSize));
        checkValueIsPositive(maxFrequency, MessageFormat.format("max frequency {0} should > 0", maxFrequency));
        this.maxSize = maxSize;
        this.evictionFactor = evictionFactor;
        this.maxFrequency = maxFrequency;
    }
    
    private void checkEvictionFactor(float evictionFactor) {
        if (evictionFactor > 1.0F || evictionFactor <= 0) {
            throw new IllegalArgumentException(MessageFormat.format("eviction factor {0} should be in (0 .. 1.0]", evictionFactor));
        }
    }
    
    private void checkValueIsPositive(int value, String message) {
        if (value < 1) {
            throw new IllegalArgumentException(message);
        }
    }
    
    @Override
    public Class<K> getKeyType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<V> getValueType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isStoreByValue() {
        return false;
    }

    @Override
    public int getMaxSize() {
        return maxSize;
    }

    @Override
    public float getEvictionFactor() {
        return evictionFactor;
    }

    @Override
    public int getMaxFrequency() {
        return maxFrequency;
    }    
    
}
