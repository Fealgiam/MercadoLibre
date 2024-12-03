package com.mercadolibre.coupon.configuration;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.ArrayList;

import static com.mercadolibre.coupon.crosscutting.constant.Constants.CACHE_CALL_APIS;

@Configuration
@EnableCaching
@EnableScheduling
public class CacheConfiguration {

    @Bean
    public CacheManager mercadoLibreCacheManager() {
        var caches = new ArrayList<Cache>();
        var cacheManager = new SimpleCacheManager();

        // Add caches call rest APIs
        CACHE_CALL_APIS.forEach(cacheName -> caches.add(new ConcurrentMapCache(cacheName)));

        // Add caches to cache manager
        cacheManager.setCaches(caches);

        return cacheManager;
    }

}
