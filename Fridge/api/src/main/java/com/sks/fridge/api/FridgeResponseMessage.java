package com.sks.fridge.api;

import com.sks.base.api.BaseMessage;

public class FridgeResponseMessage extends BaseMessage {
    private String message;
    private FridgeDTO fridgeContent;
    private boolean wasSuccess;

    public FridgeResponseMessage() {
    }

    public FridgeResponseMessage(String message) {
        this.message = message;
    }

    public FridgeDTO getFridgeContent() {
        return fridgeContent;
    }

    public void setFridgeContent(FridgeDTO fridgeContent) {
        this.fridgeContent = fridgeContent;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isWasSuccess() {
        return wasSuccess;
    }

    public void setWasSuccess(boolean wasSuccess) {
        this.wasSuccess = wasSuccess;
    }
}
