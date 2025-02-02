package com.malinouski.cache.multitenancy.db;

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
        return dataSource.getConnection();
    }

    @Override
    public void releaseAnyConnection(final Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public Connection getConnection(final String tenantIdentifier) throws SQLException {
        log.info("Getting connection for tenant {}", tenantIdentifier);
        var connection = this.dataSource.getConnection();
        connection.setSchema(tenantIdentifier);
        log.info("Connection obtained for tenant {}", tenantIdentifier);
        return connection;
    }


    @Override
    public void releaseConnection(final String tenantIdentifier, final Connection connection) throws SQLException {
//        connection.setSchema("PUBLIC");
        connection.setSchema(defaultTenant);
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

