package com.sks.base.api.exceptions;

/**
 * Exception thrown when a gateway timeout occurs.
 * This exception is a runtime exception and can be used to indicate
 * that a request to an external service has timed out.
 */
public class GatewayTimeoutException extends RuntimeException {

    /**
     * Constructs a new GatewayTimeoutException with the specified detail message.
     *
     * @param message the detail message
     */
    public GatewayTimeoutException(String message) {
        super(message);
    }
}