package com.malinouski.cache.redis.cache;

import com.malinouski.cache.multitenancy.context.TenantContext;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class CacheConfig implements CachingConfigurer {

    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            String tenantId = TenantContext.getCurrentTenant();
            return tenantId + ":" + method.getName() + ":" + Arrays.deepHashCode(params);
        };
    }
}