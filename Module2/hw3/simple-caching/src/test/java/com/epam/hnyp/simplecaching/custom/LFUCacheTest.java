package com.epam.hnyp.simplecaching.custom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import javax.cache.Cache.Entry;
import javax.cache.CacheManager;
import javax.cache.configuration.CompleteConfiguration;

import org.apache.commons.collections.IteratorUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LFUCacheTest {

    private static final String CACHE_NAME = "cacheName1";
    
    @Mock
    private CacheManager mockCacheManager;
    @Mock
    private LFUConfiguration<String, String> mockLFUConfiguration;
    
    private LFUCache<String, String> lfuCache;
    
    @Before
    public void setUp() {
        when(mockLFUConfiguration.getEvictionFactor()).thenReturn(0.75F);
        when(mockLFUConfiguration.getMaxFrequency()).thenReturn(4);
        when(mockLFUConfiguration.getMaxSize()).thenReturn(4);
        lfuCache = new LFUCache<>(mockCacheManager, mockLFUConfiguration, CACHE_NAME);
        when(mockCacheManager.<String,String>getCache(CACHE_NAME)).thenReturn(lfuCache);
    }
    
    @Test
    public void shouldStoreValue_whenPutValue() {
        lfuCache.put("key", "value");
        String storedValue = lfuCache.get("key");
        assertEquals("value", storedValue);
    }
    
    @Test
    public void shouldContainKey_whenValueWithKeyWasAdded() {
        lfuCache.put("key", "value");
        boolean result = lfuCache.containsKey("key");
        assertTrue(result);
    }
    
    @Test
    public void shouldEvictKeys_whenMaximumSizeReached() {
        lfuCache.put("key1", "value");
        lfuCache.put("key2", "value");
        lfuCache.put("key3", "value");
        lfuCache.put("key4", "value");
        lfuCache.put("key5", "value");
        List<Entry<String,String>> cacheEntries = IteratorUtils.toList(lfuCache.iterator());
        assertEquals(2, cacheEntries.size());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentException_whenConfigurationImplemetationIsNotSupported() {
        CompleteConfiguration<String, String> config = lfuCache.getConfiguration(CompleteConfiguration.class);
    }
    
    @Test
    public void shouldRemoveAllKeys_whenClearCache() {
        lfuCache.put("key1", "value");
        lfuCache.put("key2", "value");
        lfuCache.clear();
        List<Entry<String,String>> cacheEntries = IteratorUtils.toList(lfuCache.iterator());
        assertTrue(cacheEntries.isEmpty());
    }
    
    @Test(expected = IllegalStateException.class)
    public void shouldThrowIllegalStateException_whenPerformClearOnClosedCache() {
        lfuCache.close();
        lfuCache.clear();
    }
    
    @Test(expected = IllegalStateException.class)
    public void shouldThrowIllegalStateException_whenPerformPutOnClosedCache() {
        lfuCache.close();
        lfuCache.put("val", "val");
    }
    
    @Test(expected = IllegalStateException.class)
    public void shouldThrowIllegalStateException_whenPerformRemoveOnClosedCache() {
        lfuCache.close();
        lfuCache.remove("val");
    }
    
    @Test(expected = IllegalStateException.class)
    public void shouldThrowIllegalStateException_whenPerformGetOnClosedCache() {
        lfuCache.close();
        lfuCache.get("val");
    }
    
    @Test(expected = IllegalStateException.class)
    public void shouldThrowIllegalStateException_whenPerformContainsKeyOnClosedCache() {
        lfuCache.close();
        lfuCache.containsKey("val");
    }
    
    @Test(expected = IllegalStateException.class)
    public void shouldThrowIllegalStateException_whenIteratorOnClosedCache() {
        lfuCache.close();
        lfuCache.iterator();
    }
    
    @Test(expected = IllegalStateException.class)
    public void shouldThrowIllegalStateException_whenPerformReplaceOnClosedCache() {
        lfuCache.close();
        lfuCache.replace("val", "val");
    }
    
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerException_whenPerformGetForNullKey() {
        lfuCache.get(null);
    }
    
    @Test
    public void shouldCallCacheManagerDestroyCache_whenPerformCacheClose() {
        lfuCache.close();
        verify(mockCacheManager).destroyCache(CACHE_NAME);
    }
    
    @Test
    public void shouldReturnPreviousValue_whenPerformGetAndPut() {
        lfuCache.put("key", "first value");
        String previousValue = lfuCache.getAndPut("key", "new value");
        assertEquals("first value", previousValue);
    }
    
    @Test
    public void shouldReturnTrue_whenPerformIsClosedAndCacheManagerIsClosed() {
        when(mockCacheManager.isClosed()).thenReturn(true);
        boolean result = lfuCache.isClosed();
        assertTrue(result);
    }
    
    @Test
    public void shouldReturnTrue_whenPerformIsClosedAndCacheManagerDoesntOwnCache() {
        when(mockCacheManager.<String, String>getCache(CACHE_NAME)).thenReturn(lfuCache);
        boolean result = lfuCache.isClosed();
        assertTrue(result);
    }
    
    @Test
    public void shouldReturnTrue_whenPerformReplaceAndKeyIsReplacedForExistingKey() {
        lfuCache.put("key", "first value");
        boolean result = lfuCache.replace("key", "new value");
        assertTrue(result);
    }
    
    @Test
    public void shouldReturnFalse_whenPerformReplaceForNewKey() {
        lfuCache.put("key", "first value");
        boolean result = lfuCache.replace("key1", "new value");
        assertFalse(result);
    }
    
    @Test
    public void shouldReturnFalse_whenPerformRemoveForNewKey() {
        lfuCache.put("key", "first value");
        boolean result = lfuCache.remove("key1");
        assertFalse(result);
    }

    @Test
    public void shouldReturnTrue_whenPerformRemoveForExistentKey() {
        lfuCache.put("key", "first value");
        boolean result = lfuCache.remove("key");
        assertTrue(result);
    }
}
