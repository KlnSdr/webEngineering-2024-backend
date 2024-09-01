package com.sks.fridge.api;

import com.sks.base.api.BaseMessage;

public class FridgeResponseMessage extends BaseMessage {
    private FridgeDTO fridgeContent;
    private boolean wasSuccess;

    public FridgeResponseMessage() {
    }

    public FridgeDTO getFridgeContent() {
        return fridgeContent;
    }

    public void setFridgeContent(FridgeDTO fridgeContent) {
        this.fridgeContent = fridgeContent;
    }

    public boolean isWasSuccess() {
        return wasSuccess;
    }

    public void setWasSuccess(boolean wasSuccess) {
        this.wasSuccess = wasSuccess;
    }
}
