package com.sks.fridge.api;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * Defines the behavior of a listener for fridge-related messages.
 * This interface is intended to be implemented by classes that handle
 * incoming messages from a specified RabbitMQ queue.
 */
public interface FridgeListener {
    /**
     * Listens for incoming messages on the {@link FridgeQueueConfig#REQUEST_QUEUE_NAME} queue.
     * This method is triggered by messages arriving at the specified queue and processes
     * them as {@link FridgeRequestMessage} instances.
     *
     * @param in The incoming fridge request message to be processed.
     */
    @RabbitListener(queues = FridgeQueueConfig.REQUEST_QUEUE_NAME)
    void listen(Message in); // FIXME: Change to FridgeRequestMessage
//    void listen(FridgeRequestMessage in);
}
