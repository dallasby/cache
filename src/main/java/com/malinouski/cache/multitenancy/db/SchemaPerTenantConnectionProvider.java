package com.malinouski.cache.multitenancy.db;

import com.malinouski.cache.multitenancy.context.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class SchemaPerTenantConnectionProvider implements
        MultiTenantConnectionProvider<String>,
        HibernatePropertiesCustomizer {
    private final DataSource dataSource;

    @Value("${multitenant.defaultTenant}")
    String defaultTenant;

    @Override
    public Connection getAnyConnection() throws SQLException {
        return this.dataSource.getConnection();
    }

    @Override
    public void releaseAnyConnection(final Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public Connection getConnection(final String tenantIdentifier) throws SQLException {
        String tenantId = tenantIdentifier != null ? tenantIdentifier : TenantContext.getCurrentTenant();

        if (tenantId == null) {
            tenantId = defaultTenant;
            log.info("No tenantId identifier provided. Using default tenantId: {}", defaultTenant);
        }

        log.info("Getting connection for tenantId {}", tenantId);
        var connection = this.dataSource.getConnection();
        connection.setSchema(tenantId);
        log.info("Connection obtained for tenantId {}", tenantId);
        return connection;
    }


    @Override
    public void releaseConnection(final String tenantIdentifier, final Connection connection) throws SQLException {
        String tenant = tenantIdentifier != null ? tenantIdentifier : TenantContext.getCurrentTenant();

        if (tenant == null) {
            tenant = defaultTenant;
        }

        log.info("Releasing connection for tenant {}", tenant);
        connection.setSchema(tenant);
        connection.close();
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    public boolean isUnwrappableAs(final Class<?> aClass) {
        return false;
    }

    @Override
    public <T> T unwrap(final Class<T> aClass) {
        return null;
    }

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put(AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER, this);
    }
}

