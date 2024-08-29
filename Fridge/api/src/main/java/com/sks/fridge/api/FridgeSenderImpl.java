package com.sks.fridge.api;

import com.sks.base.api.BaseSenderImpl;
import com.sks.base.api.exceptions.MessageConversionException;
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
        final FridgeResponseMessage errResponse = createErrorResponse("Invalid response");
        errResponse.setException(new MessageConversionException("Invalid response"));
        return errResponse;
    }

    @Override
    protected FridgeResponseMessage createErrorResponse(String errorMessage) {
        FridgeResponseMessage response = new FridgeResponseMessage();
        response.setDidError(true);
        response.setErrorMessage(errorMessage);
        return response;
    }
}
