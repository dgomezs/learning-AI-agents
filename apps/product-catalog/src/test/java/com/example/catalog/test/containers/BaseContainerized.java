package com.example.catalog.test.containers;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.Container;

import java.util.Map;

/**
 * Base class for Testcontainer implementations.
 * Provides common functionality for container lifecycle management.
 */
public abstract class BaseContainerized implements QuarkusTestResourceLifecycleManager {
    
    private static final Logger LOG = LoggerFactory.getLogger(BaseContainerized.class);
    
    /**
     * Configure and start the container.
     * 
     * @return Map of properties to be added to the Quarkus test configuration.
     */
    @Override
    public abstract Map<String, String> start();
    
    /**
     * Stop the container if required.
     * Default implementation does nothing as TestContainers handles container shutdown.
     */
    @Override
    public void stop() {
        LOG.info("Container cleanup handled by TestContainers");
    }
    
    /**
     * Helper method to log container startup.
     * 
     * @param containerType The type of container being started
     * @param container The container instance
     */
    protected void logContainerStart(String containerType, Container container) {
        LOG.info("{} container started: {}", containerType, container.getDockerImageName());
    }
    
    /**
     * Helper method to log container configuration.
     * 
     * @param config The configuration map
     */
    protected void logConfiguration(Map<String, String> config) {
        LOG.info("Container configuration:");
        config.forEach((key, value) -> {
            // Mask sensitive information
            if (key.contains("password") || key.contains("secret")) {
                LOG.info("  {} = {}", key, "********");
            } else {
                LOG.info("  {} = {}", key, value);
            }
        });
    }
}
