package com.example.productcatalog.test.containers;

import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.utility.DockerImageName;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka Testcontainer setup for integration tests of event-driven components.
 * This container will be shared across all tests that use this resource.
 */
public class KafkaTestContainer extends BaseContainerized {

    private static final DockerImageName KAFKA_IMAGE = DockerImageName.parse("confluentinc/cp-kafka:7.2.1");
    private static final Network NETWORK = Network.newNetwork();
    
    private static final KafkaContainer KAFKA_CONTAINER = new KafkaContainer(KAFKA_IMAGE)
            .withNetwork(NETWORK)
            .withReuse(true);

    @Override
    public Map<String, String> start() {
        KAFKA_CONTAINER.start();
        
        logContainerStart("Kafka", KAFKA_CONTAINER);
        
        Map<String, String> config = new HashMap<>();
        config.put("kafka.bootstrap.servers", KAFKA_CONTAINER.getBootstrapServers());
        config.put("mp.messaging.connector.smallrye-kafka.bootstrap.servers", KAFKA_CONTAINER.getBootstrapServers());
        
        logConfiguration(config);
        
        return config;
    }
}