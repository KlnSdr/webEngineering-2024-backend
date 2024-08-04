package com.sks.fridge.api;

import com.sks.base.api.BaseMessage;

public class FridgeResponseMessage extends BaseMessage {
    private String message;

    public FridgeResponseMessage() {
    }

    public FridgeResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
