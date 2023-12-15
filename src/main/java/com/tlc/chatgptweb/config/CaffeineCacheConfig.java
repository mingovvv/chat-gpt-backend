package com.tlc.chatgptweb.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Scheduler;
import com.tlc.chatgptweb.cache.CacheType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@EnableCaching
@Configuration
public class CaffeineCacheConfig {

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        List<CaffeineCache> caches = Arrays.stream(CacheType.values()).map(cache -> new CaffeineCache(
                cache.getName(), Caffeine.newBuilder()
                .scheduler(Scheduler.systemScheduler())
                .expireAfterWrite(cache.getExpired())
                .maximumSize(cache.getMaximumSize()) // cache max entries
                .removalListener((key, value, cause) -> {
                    if (cause.wasEvicted()) {
                        log.info("This key has expired : [{}]", key);
                    } else {
                        log.info("This key has evicted : [{}]", key);
                    }
                })
//                .recordStats() // 통계 정보
                .build()
        )).collect(Collectors.toList());
        cacheManager.setCaches(caches);
        return cacheManager;
    }

}