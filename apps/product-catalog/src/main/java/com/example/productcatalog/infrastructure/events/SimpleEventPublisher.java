package com.example.productcatalog.infrastructure.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * A simple implementation of the EventPublisher that logs events.
 * This is just for testing until we implement the Kafka producer.
 */
@ApplicationScoped
public class SimpleEventPublisher implements EventPublisher {
    
    private static final Logger LOG = LoggerFactory.getLogger(SimpleEventPublisher.class);
    
    @Override
    public <T> void publish(T event) {
        LOG.info("Event published: {}", event);
    }
}