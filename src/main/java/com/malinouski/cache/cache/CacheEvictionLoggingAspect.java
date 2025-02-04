package com.malinouski.cache.cache;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class CacheEvictionLoggingAspect {
    @Before("@annotation(org.springframework.cache.annotation.CacheEvict) && @annotation(cacheEvict)")
    public void logBeforeEviction(CacheEvict cacheEvict) {
        log.info("Cache eviction initiated. Cache: {}, All Entries: {}",
                String.join(", ", cacheEvict.value()),
                cacheEvict.allEntries());
    }

    @After("@annotation(org.springframework.cache.annotation.CacheEvict) && @annotation(cacheEvict)")
    public void logAfterEviction(CacheEvict cacheEvict) {
        log.info("Cache eviction completed. Cache: {}, All Entries: {}",
                String.join(", ", cacheEvict.value()),
                cacheEvict.allEntries());
    }
}
