package com.sks.base.api;

import java.io.Serializable;
import java.util.UUID;

/**
 * Represents a base message with a unique correlation ID.
 * This class is used as a foundation for all messages that are sent between services.
 */
public class BaseMessage implements Serializable {
    private final UUID correlationId;
    private boolean didError = false;
    private Exception exception;
    private String errorMessage;

    public BaseMessage() {
        this.correlationId = UUID.randomUUID();
    }

    public String getCorrelationId() {
        return correlationId.toString();
    }

    public boolean didError() {
        return didError;
    }

    public void setDidError(boolean didError) {
        this.didError = didError;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
