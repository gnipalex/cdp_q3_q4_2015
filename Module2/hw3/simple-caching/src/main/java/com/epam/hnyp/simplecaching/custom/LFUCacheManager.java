package com.epam.hnyp.simplecaching.custom;

import java.net.URI;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.cache.configuration.Configuration;
import javax.cache.spi.CachingProvider;

public class LFUCacheManager implements CacheManager {

    private boolean isClosed;
    private Map<String, Cache<?, ?>> cachesMap; 
    
    public LFUCacheManager() {
        cachesMap = new ConcurrentHashMap<>();
    }
    
    public void close() {
        if (isClosed) {
            return;
        }
        for (Cache<?, ?> cache : cachesMap.values()) {
            try {
              cache.close();  
            } catch (Exception e) { }
        }
    }

    public <K, V, C extends Configuration<K, V>> Cache<K, V> createCache(String name, C config) throws IllegalArgumentException {
        if (cachesMap.containsKey(name)) {
            throw new CacheException(MessageFormat.format("cache with name {0} already exists", name));
        }
        LFUCache<K, V> cache = new LFUCache<K, V>(this, config, name);
        cachesMap.put(name, cache);
        return cache;
    }

    public void destroyCache(String name) {
        Cache<?,?> cacheToDestroy = cachesMap.get(name);
        if (cacheToDestroy != null) {
            cacheToDestroy.clear();
            cacheToDestroy.close();
            cachesMap.remove(name);
        }
    }

    public void enableManagement(String arg0, boolean arg1) {
        throw new UnsupportedOperationException();
    }

    public void enableStatistics(String arg0, boolean arg1) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public <K, V> Cache<K, V> getCache(String name) {
        return (Cache<K, V>) cachesMap.get(name);
    }

    public <K, V> Cache<K, V> getCache(String arg0, Class<K> arg1, Class<V> arg2) {
        throw new UnsupportedOperationException();
    }

    public Iterable<String> getCacheNames() {
        return cachesMap.keySet();
    }

    public CachingProvider getCachingProvider() {
        return null;
    }

    public ClassLoader getClassLoader() {
        throw new UnsupportedOperationException();
    }

    public Properties getProperties() {
        throw new UnsupportedOperationException();
    }

    public URI getURI() {
        throw new UnsupportedOperationException();
    }

    public boolean isClosed() {
        return isClosed;
    }

    public <T> T unwrap(Class<T> arg0) {
        throw new UnsupportedOperationException();
    }

}
