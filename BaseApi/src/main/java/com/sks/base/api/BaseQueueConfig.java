package com.sks.base.api;

/**
 * Interface representing the configuration for a queue in the system.
 * Provides methods to retrieve the names of the request and response queues,
 * the exchange name, and the request routing key.
 */
public interface BaseQueueConfig {

    /**
     * Gets the name of the request queue.
     *
     * @return the name of the request queue
     */
    String getRequestQueueName();

    /**
     * Gets the name of the response queue.
     *
     * @return the name of the response queue
     */
    String getResponseQueueName();

    /**
     * Gets the name of the exchange.
     *
     * @return the name of the exchange
     */
    String getExchangeName();

    /**
     * Gets the routing key for the request.
     *
     * @return the request routing key
     */
    String getRequestRoutingKey();
}