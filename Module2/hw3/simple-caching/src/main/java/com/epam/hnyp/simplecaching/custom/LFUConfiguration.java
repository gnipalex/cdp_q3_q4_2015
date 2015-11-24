package com.epam.hnyp.simplecaching.custom;

import javax.cache.configuration.Configuration;

public interface LFUConfiguration<K, V> extends Configuration<K, V> {
    int getMaxSize();
    float getEvictionFactor();
    int getMaxFrequency();
}
