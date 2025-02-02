package com.malinouski.cache.multitenancy.service.impl;

import com.malinouski.cache.multitenancy.service.TenantProvisioningService;
import com.malinouski.cache.multitenancy.util.TenantUtil;
import liquibase.command.CommandScope;
import liquibase.exception.LiquibaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.regex.Pattern;

import static liquibase.command.core.UpdateCountSqlCommandStep.CHANGELOG_FILE_ARG;
import static liquibase.command.core.helpers.DbUrlConnectionArgumentsCommandStep.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TenantProvisioningServiceImpl implements TenantProvisioningService {
    public static final String LIQUIBASE_PATH = "db/changelog/db.changelog-master.yaml";
    private static final Pattern TENANT_PATTERN = Pattern.compile("[-\\w]+");
    private static final String CREATE_SCHEMA_QUERY = "CREATE SCHEMA IF NOT EXISTS %s";
    private static final String DROP_SCHEMA_QUERY = "DROP SCHEMA IF EXISTS %s CASCADE";
    private final DataSource dataSource;

    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    @Override
    public void subscribeTenant(final String tenantId) {
        log.info("Subscribing tenant {}", tenantId);
        Validate.isTrue(isValidTenantId(tenantId), String.format("Invalid tenant id: \"%s\"", tenantId));
        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            var schemaName = TenantUtil.createSchemaName(tenantId);
            log.info("Creating schema {}", schemaName);

            statement.executeUpdate(String.format(CREATE_SCHEMA_QUERY, schemaName));

            log.info("Schema {} created successfully", schemaName);

            var commandScope = new CommandScope("update");

            commandScope.addArgumentValue(CHANGELOG_FILE_ARG, LIQUIBASE_PATH);
            commandScope.addArgumentValue(URL_ARG, url);
            commandScope.addArgumentValue(USERNAME_ARG, username);
            commandScope.addArgumentValue(PASSWORD_ARG, password);
            commandScope.addArgumentValue(DEFAULT_SCHEMA_NAME_ARG, schemaName);

            commandScope.execute();
        } catch (SQLException | LiquibaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void unsubscribeTenant(String tenantId) {
        Validate.isTrue(isValidTenantId(tenantId), String.format("Invalid tenant id: \"%s\"", tenantId));
        log.info("Unsubscribing tenant: {}", tenantId);
        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            var schemaName = TenantUtil.createSchemaName(tenantId);
            statement.execute(String.format(DROP_SCHEMA_QUERY, schemaName));
        } catch (SQLException e) {
            throw new RuntimeException("Tenant provisioning failed: " + e.getMessage(), e);
        }

    }

    private boolean isValidTenantId(final String tenantId) {
        log.info("Validating tenant id: {}", tenantId);
        return tenantId != null && TENANT_PATTERN.matcher(tenantId).matches();
    }
}
