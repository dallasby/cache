package com.malinouski.cache.multitenancy.resolvers;

import com.malinouski.cache.multitenancy.context.TenantContext;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class TenantIdentifierResolver implements
        CurrentTenantIdentifierResolver<String>,
        HibernatePropertiesCustomizer {
    @Value("${multitenant.defaultTenant}")
    String defaultTenant;

    @Override
    public String resolveCurrentTenantIdentifier() {
        log.info("Resolving tenant identifier");

        String tenantId = TenantContext.getCurrentTenant();
        if (tenantId == null) {
            log.info("No current tenant set. Using default tenant: {}", defaultTenant);
            tenantId = defaultTenant;
        }

        return tenantId;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return false;
    }

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, this);
    }
}
