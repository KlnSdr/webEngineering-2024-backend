package com.sks.users.api;

import com.sks.base.api.BaseSender;

public interface UsersSender extends BaseSender<UsersRequestMessage, UsersResponseMessage> {
    UsersResponseMessage sendRequest(UsersRequestMessage message);
    void sendResponse(UsersRequestMessage request, UsersResponseMessage response);
}
