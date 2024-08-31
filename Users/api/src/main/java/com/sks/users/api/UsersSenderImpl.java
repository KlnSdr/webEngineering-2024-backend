package com.sks.users.api;

import com.sks.base.api.BaseSenderImpl;
import com.sks.base.api.exceptions.MessageConversionException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Implementation of the UsersSender interface.
 * This class extends the BaseSenderImpl class and provides implementations
 * for sending user request and response messages using AMQP.
 */
@Component
public class UsersSenderImpl extends BaseSenderImpl<UsersRequestMessage, UsersResponseMessage, UsersQueueConfig> implements UsersSender {

    /**
     * Constructs a UsersSenderImpl with the specified AMQP template and queue configuration.
     *
     * @param amqpTemplate the AMQP template to use for sending messages
     * @param config the queue configuration
     */
    public UsersSenderImpl(@Qualifier("usersRabbitTemplate") AmqpTemplate amqpTemplate, UsersQueueConfig config) {
        super(amqpTemplate, config);
    }

    /**
     * Converts the response object to a UsersResponseMessage.
     *
     * @param response the response object to convert
     * @return the converted UsersResponseMessage, or an error response if the conversion fails
     */
    @Override
    protected UsersResponseMessage convertResponse(Object response) {
        if (response instanceof UsersResponseMessage) {
            return (UsersResponseMessage) response;
        }
        final UsersResponseMessage errResponse = createErrorResponse("Invalid response");
        errResponse.setException(new MessageConversionException("Invalid response"));
        return errResponse;
    }

    /**
     * Creates an error response with the specified error message.
     *
     * @param errorMessage the error message to include in the response
     * @return a UsersResponseMessage containing the error message
     */
    @Override
    protected UsersResponseMessage createErrorResponse(String errorMessage) {
        UsersResponseMessage response = new UsersResponseMessage();
        response.setErrorMessage(errorMessage);
        response.setDidError(true);
        return response;
    }
}