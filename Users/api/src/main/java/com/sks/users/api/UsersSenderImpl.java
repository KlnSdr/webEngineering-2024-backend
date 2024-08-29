package com.sks.users.api;

import com.sks.base.api.BaseSenderImpl;
import com.sks.base.api.exceptions.MessageConversionException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class UsersSenderImpl extends BaseSenderImpl<UsersRequestMessage, UsersResponseMessage, UsersQueueConfig> implements UsersSender {

    public UsersSenderImpl(@Qualifier("usersRabbitTemplate") AmqpTemplate amqpTemplate, UsersQueueConfig config) {
        super(amqpTemplate, config);
    }

    @Override
    protected UsersResponseMessage convertResponse(Object response) {
        if (response instanceof UsersResponseMessage) {
            return (UsersResponseMessage) response;
        }
        final UsersResponseMessage errResponse = createErrorResponse("Invalid response");
        errResponse.setException(new MessageConversionException("Invalid response"));
        return errResponse;
    }

    @Override
    protected UsersResponseMessage createErrorResponse(String errorMessage) {
        UsersResponseMessage response = new UsersResponseMessage();
        response.setErrorMessage(errorMessage);
        response.setDidError(true);
        return response;
    }
}
