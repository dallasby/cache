package com.malinouski.cache.multitenancy.context;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class TenantContext {
    private static final ThreadLocal<String> currentTenant = new InheritableThreadLocal<>();

    public static String getCurrentTenant() {
        String tenant = currentTenant.get();
        log.info("Current Tenant: {}", tenant);
        return tenant;
    }

    public static void setCurrentTenant(String tenantId) {
        log.info("Setting Tenant: {}", tenantId);
        currentTenant.set(tenantId);
    }

    public static void clear() {
        log.info("Clearing Tenant Context");
        currentTenant.remove();
    }
}
