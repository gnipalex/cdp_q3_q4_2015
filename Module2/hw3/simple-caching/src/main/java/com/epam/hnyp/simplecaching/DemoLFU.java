package com.epam.hnyp.simplecaching;

import java.util.Iterator;

import javax.cache.Cache;
import javax.cache.Cache.Entry;

import com.epam.hnyp.simplecaching.custom.LFUCacheManager;
import com.epam.hnyp.simplecaching.custom.LFUConfigurationImpl;

public class DemoLFU {
    
    public static void demoLFU() {
        LFUCacheManager cacheManager = new LFUCacheManager();
        cacheManager.createCache("myCache", new LFUConfigurationImpl<String, Integer>(5, 0.8F, 5));
        Cache<String, Integer> cache = cacheManager.getCache("myCache");
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
    
    public static void printEntries(Cache cache) {
        for (Iterator<Entry> it = cache.iterator(); it.hasNext(); ) {
            System.out.println(it.next());
        }
        System.out.println("##################");
    }
    
    public static void main(String[] args) {
        demoLFU();
    }
}
