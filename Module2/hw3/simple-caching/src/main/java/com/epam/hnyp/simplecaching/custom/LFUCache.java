package com.epam.hnyp.simplecaching.custom;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.CacheEntryListenerConfiguration;
import javax.cache.configuration.Configuration;
import javax.cache.integration.CompletionListener;
import javax.cache.processor.EntryProcessor;
import javax.cache.processor.EntryProcessorException;
import javax.cache.processor.EntryProcessorResult;

public class LFUCache<K, V> implements Cache<K, V> {

    private boolean isClosed;
    private String name;
    private CacheManager cacheManager;
    private LFUConfiguration<K,V> configuration;
    private Map<K, LFUEntry<K, V>> cacheEntries;
    private List< Map<K,LFUEntry<K,V>> > frequencyList;
    
    public LFUCache(CacheManager cacheManager, Configuration<K, V> configuration, String name) {
        this.cacheManager = cacheManager;
        checkConfiguratonTypeIsSupported(configuration.getClass());
        this.configuration = (LFUConfiguration<K, V>) configuration;
        this.name = name;
        this.cacheEntries = new ConcurrentHashMap<>();
        initializeFrequencyList();
    }

    private void initializeFrequencyList() {
        this.frequencyList = new LinkedList<>();
        for (int i=0; i < configuration.getMaxFrequency(); i++) {
            frequencyList.add(i, new LinkedHashMap<>());
        }
    }
    
    public synchronized void clear() {
        assertIsNotClosed();
        this.cacheEntries.clear();
        this.frequencyList.clear();
    }
    
    private void doEviction() {
        int howManyToEvict = Math.round(cacheEntries.size() * configuration.getEvictionFactor());
        for (Map<K, LFUEntry<K, V>> frequencyItem : frequencyList) {
            Iterator<java.util.Map.Entry<K, LFUEntry<K, V>>> freqItemIterator = frequencyItem.entrySet().iterator();
            while (freqItemIterator.hasNext() && howManyToEvict > 0) {
                K key = freqItemIterator.next().getKey();
                freqItemIterator.remove();
                cacheEntries.remove(key);
                howManyToEvict--;
            }
            if (howManyToEvict == 0) {
                break;
            }
        }
    }

    public synchronized void close() {
        if (!isClosed && cacheManager != null) {
            isClosed = true;
            cacheManager.destroyCache(name);
        }  
    }

    public synchronized boolean containsKey(K key) {
        assertNotNull(key);
        assertIsNotClosed();
        return cacheEntries.containsKey(key);
    }

    public void deregisterCacheEntryListener(
            CacheEntryListenerConfiguration<K, V> arg0) {
        throw new UnsupportedOperationException();        
    }
    
    private void assertNotNull(Object obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
    }
    
    private void assertIsNotClosed() {
        if (isClosed()) {
            throw new IllegalStateException("cache is closed");
        } 
    }

    public synchronized V get(K key) {
        assertNotNull(key);
        assertIsNotClosed();
        if (!containsKey(key)) {
            return null;
        }
        LFUEntry<K, V> entry = cacheEntries.get(key);
        increaseFrequency(entry);
        return entry.getValue();
    }
    
    private void increaseFrequency(LFUEntry<K, V> entry) {
        int freq = entry.frequency;
        if (freq == 0) {
            increaseAndAddToNextFrequencyItem(entry);
        } else {
            removeFromPreviousFrequencyItem(entry);
            increaseAndAddToNextFrequencyItem(entry);
        }
    }
    
    private void increaseAndAddToNextFrequencyItem(LFUEntry<K, V> entry) {
        if (entry.frequency < frequencyList.size()) {
            entry.frequency++;
        }
        Map<K, LFUEntry<K,V>> frequencyItem = frequencyList.get(entry.frequency - 1);
        frequencyItem.put(entry.key, entry);
    }
    
    private void removeFromPreviousFrequencyItem(LFUEntry<K, V> entry) {
        Map<K, LFUEntry<K,V>> frequencyItem = frequencyList.get(entry.frequency - 1);
        frequencyItem.remove(entry.key);
    }

    public Map<K, V> getAll(Set<? extends K> arg0) {
        throw new UnsupportedOperationException();
    }

    public synchronized V getAndPut(K key, V value) {
        assertNotNull(key);
        assertNotNull(value);
        assertIsNotClosed();
        LFUEntry<K, V> previousEntry = cacheEntries.get(key);
        LFUEntry<K, V> newEntry = new LFUEntry<>(key, value);
        V previousValue = null;
        if (previousEntry != null) {
            previousValue = previousEntry.value;
            newEntry.frequency = previousEntry.frequency;
            removeFromPreviousFrequencyItem(previousEntry);
        } 
        increaseAndAddToNextFrequencyItem(newEntry);
        cacheEntries.put(key, newEntry);
        return previousValue;
    }

    public V getAndRemove(K arg0) {
        throw new UnsupportedOperationException();
    }

    public V getAndReplace(K arg0, V arg1) {
        throw new UnsupportedOperationException();
    }

    public CacheManager getCacheManager() {
        return cacheManager;
    }
    
    @SuppressWarnings("rawtypes")
    private void checkConfiguratonTypeIsSupported(Class<? extends Configuration> clazz) {
        if (!LFUConfiguration.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException(MessageFormat.format("cache doesn't support {0} configuration", clazz.getName()));
        }
    }

    public <C extends Configuration<K, V>> C getConfiguration(Class<C> clazz) {
        checkConfiguratonTypeIsSupported(clazz);
        return clazz.cast(configuration);
    }

    public String getName() {
        return name;
    }

    public <T> T invoke(K arg0, EntryProcessor<K, V, T> arg1, Object... arg2)
            throws EntryProcessorException {
        throw new UnsupportedOperationException();
    }

    public <T> Map<K, EntryProcessorResult<T>> invokeAll(Set<? extends K> arg0,
            EntryProcessor<K, V, T> arg1, Object... arg2) {
        throw new UnsupportedOperationException();
    }

    public boolean isClosed() {
        return isClosed || cacheManager.isClosed() || cacheManager.getCache(name) != this;
    }

    public Iterator<Entry<K, V>> iterator() {
        assertIsNotClosed();
        return cacheEntries.values().stream().map(v -> (Entry<K,V>)v).iterator();
    }

    public void loadAll(Set<? extends K> arg0, boolean arg1,
            CompletionListener arg2) {
        throw new UnsupportedOperationException();
    }

    public synchronized void put(K key, V value) {
        assertNotNull(key);
        assertNotNull(value);
        assertIsNotClosed();
        if (cacheEntries.size() == configuration.getMaxSize()) {
            doEviction();
        }
        LFUEntry<K, V> oldEntry = cacheEntries.remove(key);
        if (oldEntry != null) {
            removeFromPreviousFrequencyItem(oldEntry);
        }
        LFUEntry<K, V> newEntry = new LFUEntry<>(key, value);
        increaseAndAddToNextFrequencyItem(newEntry);
        cacheEntries.put(key, newEntry);
    }

    public void putAll(Map<? extends K, ? extends V> arg0) {
        throw new UnsupportedOperationException();
    }

    public boolean putIfAbsent(K arg0, V arg1) {
        throw new UnsupportedOperationException();
    }

    public void registerCacheEntryListener(
            CacheEntryListenerConfiguration<K, V> arg0) {
        throw new UnsupportedOperationException(); 
    }

    public synchronized boolean remove(K key) {
        assertNotNull(key);
        assertIsNotClosed();
        LFUEntry<K,V> oldEntry = cacheEntries.remove(key);
        boolean isRemoved = oldEntry != null;
        if (isRemoved) {
            removeFromPreviousFrequencyItem(oldEntry);
        }
        return isRemoved;
    }

    public boolean remove(K arg0, V arg1) {
        throw new UnsupportedOperationException();
    }

    public void removeAll() {
        throw new UnsupportedOperationException();
    }

    public void removeAll(Set<? extends K> arg0) {
        throw new UnsupportedOperationException();
    }

    public synchronized boolean replace(K key, V value) {
        assertNotNull(key);
        assertNotNull(value);
        assertIsNotClosed();
        LFUEntry<K, V> oldEntry = cacheEntries.get(key);
        boolean isReplaced = false;
        if (oldEntry != null) {
            oldEntry.value = value;
            removeFromPreviousFrequencyItem(oldEntry);
            increaseAndAddToNextFrequencyItem(oldEntry);
            isReplaced = true;
        }
        return isReplaced;
    }

    public boolean replace(K arg0, V arg1, V arg2) {
        throw new UnsupportedOperationException();
    }

    public <T> T unwrap(Class<T> arg0) {
        throw new UnsupportedOperationException();
    }
    
    @SuppressWarnings("hiding")
    public class LFUEntry<K, V> implements Entry<K, V> {

        private int frequency;
        private K key;
        private V value;
        
        public LFUEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }
        
        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public <T> T unwrap(Class<T> arg0) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public String toString() {
            return MessageFormat.format("[{0}<->{1}]", key, value);
        }
    }

}
