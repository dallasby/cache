package com.malinouski.cache.redis.cache;

import com.malinouski.cache.multitenancy.context.TenantContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Slf4j
@Configuration
public class CacheConfig implements CachingConfigurer {

    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            String tenantId = TenantContext.getCurrentTenant();

            log.info("Cache Key Generation: Tenant={}, Method={}, Params={}", tenantId, method.getName(), Arrays.toString(params));

            if (tenantId == null || tenantId.isBlank()) {
                throw new IllegalStateException("Tenant ID is null or blank. Ensure TenantContext is properly set.");
            }
            return tenantId + ":" + method.getName() + ":" + Arrays.deepHashCode(params);
        };
    }
}