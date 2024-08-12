package com.sks.users.api;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * Defines the behavior of a listener for users-related messages.
 * This interface is intended to be implemented by classes that handle
 * incoming messages from a specified RabbitMQ queue.
 */
public interface UsersListener {
    /**
     * Listens for incoming messages on the {@link UsersQueueConfig#REQUEST_QUEUE_NAME} queue.
     * This method is triggered by messages arriving at the specified queue and processes
     * them as {@link UsersRequestMessage} instances.
     *
     * @param in The incoming users request message to be processed.
     */
    @RabbitListener(queues = UsersQueueConfig.REQUEST_QUEUE_NAME)
    void listen(UsersRequestMessage in);
}
