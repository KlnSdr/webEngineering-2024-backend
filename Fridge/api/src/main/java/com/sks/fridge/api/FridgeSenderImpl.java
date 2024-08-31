package com.sks.fridge.api;

import com.sks.base.api.BaseSenderImpl;
import com.sks.base.api.exceptions.MessageConversionException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Implementation of the FridgeSender interface.
 * Extends the BaseSenderImpl class to provide specific implementations for fridge-related messages.
 */
@Component
public class FridgeSenderImpl extends BaseSenderImpl<FridgeRequestMessage, FridgeResponseMessage, FridgeQueueConfig> implements FridgeSender {

    /**
     * Constructor for FridgeSenderImpl.
     *
     * @param amqpTemplate the AMQP template used for sending messages
     * @param config the fridge queue configuration
     */
    public FridgeSenderImpl(@Qualifier("fridgeRabbitTemplate") AmqpTemplate amqpTemplate, FridgeQueueConfig config) {
        super(amqpTemplate, config);
    }

    /**
     * Converts the response object to a FridgeResponseMessage.
     *
     * @param response the response object to convert
     * @return the converted FridgeResponseMessage, or an error response if the conversion fails
     */
    @Override
    protected FridgeResponseMessage convertResponse(Object response) {
        if (response instanceof FridgeResponseMessage) {
            return (FridgeResponseMessage) response;
        }
        final FridgeResponseMessage errResponse = createErrorResponse("Invalid response");
        errResponse.setException(new MessageConversionException("Invalid response"));
        return errResponse;
    }

    /**
     * Creates an error response message.
     *
     * @param errorMessage the error message content
     * @return the created FridgeResponseMessage with the error message
     */
    @Override
    protected FridgeResponseMessage createErrorResponse(String errorMessage) {
        FridgeResponseMessage response = new FridgeResponseMessage();
        response.setDidError(true);
        response.setErrorMessage(errorMessage);
        return response;
    }
}