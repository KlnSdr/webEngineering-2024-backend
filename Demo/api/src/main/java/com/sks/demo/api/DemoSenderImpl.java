package com.sks.demo.api;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

@Component
public class DemoSenderImpl implements DemoSender {

    private final AmqpTemplate amqpTemplate;

    public DemoSenderImpl(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    @Override
    public DemoResponseMessage sendRequest(DemoRequestMessage request) {
        amqpTemplate.convertAndSend(DemoQueueConfig.EXCHANGE_NAME, DemoQueueConfig.ROUTING_KEY_REQUEST, request, messageConfig -> {
            messageConfig.getMessageProperties().setReplyTo(DemoQueueConfig.RESPONSE_QUEUE_NAME);
            messageConfig.getMessageProperties().setCorrelationId(request.getCorrelationId());
            return messageConfig;
        });

        Object response = amqpTemplate.receiveAndConvert(DemoQueueConfig.RESPONSE_QUEUE_NAME, 5000); // Timeout for 5 seconds
        if (response instanceof DemoResponseMessage) {
            return (DemoResponseMessage) response;
        } else {
            return new DemoResponseMessage("No response received");
        }
    }

    @Override
    public void sendResponse(DemoRequestMessage request, DemoResponseMessage response) {
        amqpTemplate.convertAndSend(DemoQueueConfig.RESPONSE_QUEUE_NAME, response, messageConfig -> {
            messageConfig.getMessageProperties().setCorrelationId(request.getCorrelationId());
            return messageConfig;
        });
    }
}
