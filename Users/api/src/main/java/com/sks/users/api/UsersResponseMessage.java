package com.sks.users.api;

import com.sks.base.api.BaseMessage;

public class UsersResponseMessage extends BaseMessage {
    private String message;

    public UsersResponseMessage() {
    }

    public UsersResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
