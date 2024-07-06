package com.sks.base.api;

import org.springframework.amqp.core.AmqpTemplate;

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

    protected abstract O convertResponse(Object response);

    protected abstract O createErrorResponse(String errorMessage);
}
