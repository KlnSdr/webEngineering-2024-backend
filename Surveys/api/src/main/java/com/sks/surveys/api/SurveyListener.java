package com.sks.surveys.api;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * Defines the behavior of a listener for surveys-related messages.
 * This interface is intended to be implemented by classes that handle
 * incoming messages from a specified RabbitMQ queue.
 */
public interface SurveyListener {
    /**
     * Listens for incoming messages on the {@link SurveyQueueConfig#REQUEST_QUEUE_NAME} queue.
     * This method is triggered by messages arriving at the specified queue and processes
     * them as {@link SurveyRequestMessage} instances.
     *
     * @param in The incoming surveys request message to be processed.
     */
    @RabbitListener(queues = SurveyQueueConfig.REQUEST_QUEUE_NAME)
    void listen(Message in);
}
