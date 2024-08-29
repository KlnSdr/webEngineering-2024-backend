package com.sks.gateway.common;

import com.sks.base.api.BaseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class MessageErrorHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageErrorHandler.class);

    public void handle(BaseMessage message) {
        LOGGER.error("Error while processing message");
        LOGGER.trace(message.getErrorMessage(), message.getException());
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, message.getErrorMessage(), message.getException());
    }
}
