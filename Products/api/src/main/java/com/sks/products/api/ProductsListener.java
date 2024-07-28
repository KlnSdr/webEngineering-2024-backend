package com.sks.products.api;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * Defines the behavior of a listener for demo-related messages.
 * This interface is intended to be implemented by classes that handle
 * incoming messages from a specified RabbitMQ queue.
 */
public interface ProductsListener {
    /**
     * Listens for incoming messages on the {@link ProductsQueueConfig#REQUEST_QUEUE_NAME} queue.
     * This method is triggered by messages arriving at the specified queue and processes
     * them as {@link ProductsRequestMessage} instances.
     *
     * @param in The incoming demo request message to be processed.
     */
    @RabbitListener(queues = ProductsQueueConfig.REQUEST_QUEUE_NAME)
    void listen(ProductsRequestMessage in);
}
