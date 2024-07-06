package com.sks.base.api;

import org.springframework.amqp.core.AmqpTemplate;

/**
 * Provides an abstract implementation of {@link BaseSender} for sending and receiving messages
 * using AMQP protocol with Spring's {@link AmqpTemplate}. This class is designed to be extended
 * by concrete sender implementations that specify the types of input and output messages, as well
 * as the configuration for the AMQP queues and exchanges.
 *
 * @param <I> the input message type extending {@link BaseMessage}
 * @param <O> the output message type extending {@link BaseMessage}
 * @param <C> the configuration type extending {@link BaseQueueConfig} that holds the AMQP queue
 *            and exchange configuration
 */
public abstract class BaseSenderImpl<I extends BaseMessage, O extends BaseMessage, C extends BaseQueueConfig> implements BaseSender<I, O>{
    private final AmqpTemplate amqpTemplate;
    private final C config;

    public BaseSenderImpl(AmqpTemplate amqpTemplate, C config) {
        this.amqpTemplate = amqpTemplate;
        this.config = config;
    }

    @Override
    public O sendRequest(I message) {
        amqpTemplate.convertAndSend(config.getExchangeName(), config.getRequestRoutingKey(), message, messageConfig -> {
            messageConfig.getMessageProperties().setReplyTo(config.getResponseQueueName());
            messageConfig.getMessageProperties().setCorrelationId(message.getCorrelationId());
            return messageConfig;
        });

        Object response = amqpTemplate.receiveAndConvert(config.getResponseQueueName(), 5000); // Timeout for 5 seconds
        if (response == null) {
            return createErrorResponse("No response received");
        }
        return convertResponse(response);
    }

    @Override
    public void sendResponse(I request, O response) {
        amqpTemplate.convertAndSend(config.getResponseQueueName(), response, messageConfig -> {
            messageConfig.getMessageProperties().setCorrelationId(request.getCorrelationId());
            return messageConfig;
        });
    }

    /**
     * Converts the received AMQP message into the output message type {@code O}.
     * This method is abstract and must be implemented by subclasses to define
     * the specific conversion logic based on the response received from the message broker.
     *
     * @param response The raw response received from the message broker.
     * @return An instance of the output message type {@code O}, converted from the raw response.
     */
    protected abstract O convertResponse(Object response);

    /**
     * Creates an error response of type {@code O} with the specified error message.
     * This method is abstract and must be implemented by subclasses to construct
     * an appropriate error response based on the specific output message type.
     *
     * @param errorMessage the error message to include in the response
     * @return An error response of type {@code O}.
     */
    protected abstract O createErrorResponse(String errorMessage);
}
