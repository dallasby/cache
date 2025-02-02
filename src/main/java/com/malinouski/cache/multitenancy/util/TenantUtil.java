package com.malinouski.cache.multitenancy.util;

public class TenantUtil {
    public static String createSchemaName(final String tenantId) {
        return String.format("tenant_%s", tenantId.replace("-", "_"));
    }
}
