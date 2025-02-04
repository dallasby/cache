package com.malinouski.cache.cache;

import com.malinouski.cache.multitenancy.context.TenantContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

@Slf4j
@Component
public class TenantKeyGenerator implements KeyGenerator {
    @Override
    public Object generate(Object target, Method method, Object... params) {
        log.info("Cache Key Generation: Target={}, Method={}, Params={}", target, method.getName(), Arrays.toString(params));
        return SimpleKeyGenerator.generateKey(TenantContext.getCurrentTenant(), params);
    }
}
