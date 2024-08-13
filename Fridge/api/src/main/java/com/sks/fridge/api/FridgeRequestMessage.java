package com.sks.fridge.api;

import com.sks.base.api.BaseMessage;

public class FridgeRequestMessage extends BaseMessage {
    private String message;

    public FridgeRequestMessage() {
    }

    public FridgeRequestMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
