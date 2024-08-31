package com.sks.fridge.api;

import com.sks.base.api.BaseSender;
/**
 * Interface for sending fridge-related messages.
 * Extends the BaseSender interface with specific types for fridge requests and responses.
 */
public interface FridgeSender extends BaseSender<FridgeRequestMessage, FridgeResponseMessage> {

    /**
     * Sends a request message and returns the response.
     *
     * @param message the request message to send
     * @return the response message received
     */
    FridgeResponseMessage sendRequest(FridgeRequestMessage message);

    /**
     * Sends a response message for a given request.
     *
     * @param request the original request message
     * @param response the response message to send
     */
    void sendResponse(FridgeRequestMessage request, FridgeResponseMessage response);
}