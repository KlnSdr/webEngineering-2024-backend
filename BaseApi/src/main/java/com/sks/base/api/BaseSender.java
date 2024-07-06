package com.sks.base.api;

/**
 * Defines the contract for sending requests and responses between components.
 * This interface is generic and can be implemented by any class that handles
 * the sending of messages, where {@code I} is the input message type and
 * {@code O} is the output message type. Both {@code I} and {@code O} must extend
 * {@link BaseMessage} to ensure they have a unique correlation ID.
 *
 * @param <I> the input message type extending {@link BaseMessage}
 * @param <O> the output message type extending {@link BaseMessage}
 */
public interface BaseSender<I extends BaseMessage, O extends BaseMessage> {
    /**
     * Sends a request and expects a response of type {@code O}.
     *
     * @param message the input message of type {@code I}
     * @return the response message of type {@code O}
     */
    O sendRequest(I message);
    /**
     * Sends a response back to the requester.
     *
     * @param request the original request message of type {@code I}
     * @param response the response message to be sent of type {@code O}
     */
    void sendResponse(I request, O response);
}
