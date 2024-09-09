package com.sks.gateway.common;

import com.sks.base.api.BaseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

/**
 * Component responsible for handling errors that occur while processing messages.
 * Logs the error and throws a ResponseStatusException with the appropriate HTTP status.
 */
@Component
public class MessageErrorHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageErrorHandler.class);

    /**
     * Handles the error in the given message by logging it and throwing a ResponseStatusException.
     *
     * @param message the message containing the error details
     * @throws ResponseStatusException with HTTP status 500 and the error message from the given message
     */
    public void handle(BaseMessage message) {
        LOGGER.error("Error while processing message");
        LOGGER.trace(message.getErrorMessage(), message.getException());
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, message.getErrorMessage(), message.getException());
    }
}