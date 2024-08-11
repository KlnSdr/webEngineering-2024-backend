package com.sks.users.api;

import com.sks.base.api.BaseMessage;

public class UsersRequestMessage extends BaseMessage {
    private String message;

    public UsersRequestMessage() {
    }

    public UsersRequestMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
