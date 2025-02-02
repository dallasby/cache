package com.malinouski.cache.multitenancy.resolvers;

import org.springframework.lang.Nullable;

@FunctionalInterface
public interface TenantResolver<T> {
    @Nullable
    String resolveTenantId(T object);
}
