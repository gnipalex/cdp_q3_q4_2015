package com.epam.hnyp.simplecaching;

import java.util.Iterator;

import javax.cache.Cache.Entry;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

import com.epam.hnyp.simplecaching.custom.LFUCacheManager;
import com.epam.hnyp.simplecaching.custom.LFUConfigurationImpl;

public class Demo {
    public static void main(String[] args) throws InterruptedException {
        demoLFU();
        //demoEhCache();
    }
    
    public static void demoLFU() {
        LFUCacheManager cacheManager = new LFUCacheManager();
        cacheManager.createCache("myCache", new LFUConfigurationImpl<String, Integer>(5, 0.8F, 5));
        javax.cache.Cache<String, Integer> cache = cacheManager.getCache("myCache");
        cache.put("a1", 10);
        cache.get("a1");
        cache.put("a2", 20);
        cache.get("a2"); cache.get("a2");
        cache.put("a3", 30);
        cache.get("a3");
        cache.put("a4", 40);
        cache.put("a5", 50);
        printEntries(cache);
        cache.put("a1", 10);
        printEntries(cache);
        cache.put("a6", 60);
        printEntries(cache);
        cache.remove("a3");
        printEntries(cache);
        cacheManager.close();
        try {
            cache.get("a1");
        } catch (IllegalStateException e) {
            System.out.println(e);
        }
    }
    
    public static void printEntries(javax.cache.Cache cache) {
        for (Iterator<Entry> it = cache.iterator(); it.hasNext(); ) {
            System.out.println(it.next());
        }
        System.out.println("##################");
    }
    
    public static void demoEhCache() {
        CacheManager cacheManager = CacheManager.newInstance();
        Cache simpleCache = new Cache(
                new CacheConfiguration()
                    .name("simple-cache")
                    .maxEntriesLocalHeap(1000)
                    .memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LFU)
        );
        cacheManager.addCache(simpleCache);
        simpleCache.put(new Element("key1", 100));
        Element element = simpleCache.get("key1");
        System.out.println(element != null ? element.getObjectValue() : "null"); 
        simpleCache.remove("key1");
        element = simpleCache.get("key1");
        System.out.println(element != null ? element.getObjectValue() : "null"); 
    }
    
}
