package com.epam.hnyp.simplecaching.custom;

import static org.junit.Assert.*;

import javax.cache.Cache;
import javax.cache.CacheException;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.cglib.core.ReflectUtils;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LFUCacheManagerTest {

    @Mock
    private LFUConfiguration<String, String> mockLFUConfiguration;
    @Mock
    private Cache<String, String> mockCache;
    
    @Spy
    private LFUCacheManager spyLfuCacheManager;
    
    
    @Test
    public void shouldCreateCache_whenCacheDoesnotExistForProvidedName() {
        Cache<String, String> cache1 = spyLfuCacheManager.createCache("cache1", mockLFUConfiguration);
        assertNotNull(cache1);
    }
    
    @Test(expected = CacheException.class)
    public void shouldThrowCacheException_whenCacheExistsForProviderName() {
        spyLfuCacheManager.createCache("cache1", mockLFUConfiguration);
        spyLfuCacheManager.createCache("cache1", mockLFUConfiguration);
    }
    
    @Test
    public void shouldClearAndCloseCache_whenDestroyExistingCache() {
        spyLfuCacheManager.createCache("cache1", mockLFUConfiguration);
    }
    

}
