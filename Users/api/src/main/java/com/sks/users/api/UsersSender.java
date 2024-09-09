package com.sks.users.api;

import com.sks.base.api.BaseSender;

/**
 * Interface for sending user-related messages.
 * This interface extends the BaseSender interface and provides methods
 * for sending user request and response messages.
 */
public interface UsersSender extends BaseSender<UsersRequestMessage, UsersResponseMessage> {

    /**
     * Sends a user request message and returns a response message.
     *
     * @param message the user request message to send
     * @return the user response message
     */
    UsersResponseMessage sendRequest(UsersRequestMessage message);

    /**
     * Sends a user response message for a given request.
     *
     * @param request the user request message
     * @param response the user response message to send
     */
    void sendResponse(UsersRequestMessage request, UsersResponseMessage response);
}