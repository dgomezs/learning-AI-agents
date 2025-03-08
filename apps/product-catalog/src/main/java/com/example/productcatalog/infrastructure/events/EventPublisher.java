package com.example.productcatalog.infrastructure.events;

/**
 * Interface for publishing domain events.
 */
public interface EventPublisher {
    /**
     * Publish an event to all subscribers.
     *
     * @param event the event to publish
     * @param <T> the event type
     */
    <T> void publish(T event);
}