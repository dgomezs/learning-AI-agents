package com.example.productcatalog.test.containers;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * PostgreSQL Testcontainer setup for integration tests.
 * This container will be shared across all tests that use this resource.
 */
public class PostgresTestContainer implements QuarkusTestResourceLifecycleManager {

    private static final Logger LOG = LoggerFactory.getLogger(PostgresTestContainer.class);
    private static final DockerImageName POSTGRES_IMAGE = DockerImageName.parse("postgres:13");
    private static final String DATABASE_NAME = "catalog_test_db";
    private static final String USERNAME = "test_user";
    private static final String PASSWORD = "test_password";

    private static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>(POSTGRES_IMAGE)
            .withDatabaseName(DATABASE_NAME)
            .withUsername(USERNAME)
            .withPassword(PASSWORD)
            // Enable container reuse between test runs
            .withReuse(true);

    @Override
    public Map<String, String> start() {
        POSTGRES.start();
        LOG.info("PostgreSQL container started at: {}", POSTGRES.getJdbcUrl());
        
        // Configure Quarkus to use the testcontainer
        Map<String, String> config = new HashMap<>();
        config.put("quarkus.datasource.jdbc.url", POSTGRES.getJdbcUrl());
        config.put("quarkus.datasource.username", POSTGRES.getUsername());
        config.put("quarkus.datasource.password", POSTGRES.getPassword());
        config.put("quarkus.datasource.db-kind", "postgresql");
        
        // Enable Liquibase to run migrations on startup
        config.put("quarkus.liquibase.migrate-at-start", "true");
        config.put("quarkus.liquibase.validate-on-migrate", "true");
        config.put("quarkus.liquibase.clean-at-start", "true"); // Ensures clean DB state for each test run
        
        return config;
    }

    @Override
    public void stop() {
        // Container will be stopped by Testcontainers automatically
        // No need to call POSTGRES.stop() as we're reusing containers
    }
}