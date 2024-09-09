package com.sks.base.api.exceptions;

/**
 * Exception thrown when a message conversion error occurs.
 * This exception is a runtime exception and can be used to indicate
 * that an error happened during the conversion of a message.
 */
public class MessageConversionException extends RuntimeException {

    /**
     * Constructs a new MessageConversionException with the specified detail message.
     *
     * @param message the detail message
     */
    public MessageConversionException(String message) {
        super(message);
    }
}