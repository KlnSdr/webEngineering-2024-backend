package com.sks.fridge.api;

import com.sks.base.api.BaseSenderImpl;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class FridgeSenderImpl extends BaseSenderImpl<FridgeRequestMessage, FridgeResponseMessage, FridgeQueueConfig> implements FridgeSender {

    public FridgeSenderImpl(@Qualifier("fridgeRabbitTemplate") AmqpTemplate amqpTemplate, FridgeQueueConfig config) {
        super(amqpTemplate, config);
    }

    @Override
    protected FridgeResponseMessage convertResponse(Object response) {
        if (response instanceof FridgeResponseMessage) {
            return (FridgeResponseMessage) response;
        }
        return createErrorResponse("Invalid response");
    }

    @Override
    protected FridgeResponseMessage createErrorResponse(String errorMessage) {
        FridgeResponseMessage response = new FridgeResponseMessage();
        response.setMessage(errorMessage);
        return response;
    }
}
