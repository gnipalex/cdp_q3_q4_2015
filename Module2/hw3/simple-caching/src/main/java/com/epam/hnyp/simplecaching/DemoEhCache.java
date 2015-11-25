package com.epam.hnyp.simplecaching;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

public class DemoEhCache {
    public static void main(String[] args) throws InterruptedException {
//        Cache cache = getCacheFromDynamicConfig();
        Cache cache = getCacheFromXmlConfig();
        demoEhCache(cache);
    }
    
    public static void demoEhCache(Cache cache) {
        cache.put(new Element("ann", new Human("ann", 10, false)));
        cache.get("ann");
        cache.put(new Element("tom", new Human("tom", 22, true)));
        cache.get("tom");
        cache.put(new Element("mag", "just string"));
        cache.get("mag");
        cache.put(new Element("harry", new Human("harry", 17, true)));
        cache.get("harry");
        cache.put(new Element("denis", new Human("denis", 1, true)));
        cache.getAll(cache.getKeys()).entrySet().forEach(e -> {
            System.out.println(e.getKey() + " -> " + e.getValue());
        });
        System.out.println("################");
        cache.put(new Element("overflow", "this is overflow value"));
        cache.getAll(cache.getKeys()).entrySet().forEach(e -> {
            System.out.println(e.getKey() + " -> " + e.getValue());
        }); 
    }
    
    public static Cache getCacheFromXmlConfig() {
        CacheManager cacheManager = CacheManager.newInstance();
        return cacheManager.getCache("cache1");
    }
    
    public static Cache getCacheFromDynamicConfig() {
        CacheManager cacheManager = CacheManager.newInstance();
        Cache cache = new Cache(new CacheConfiguration()
                    .name("simple-cache")
                    .maxEntriesLocalHeap(5)
                    .memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LFU)
        );
        cacheManager.addCache(cache);
        return cache;
    }
    
}
