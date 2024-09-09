package com.sks.fridge.api;

import com.sks.base.api.BaseMessage;

/**
 * Represents a response message for fridge operations.
 */
public class FridgeResponseMessage extends BaseMessage {
    private FridgeDTO fridgeContent;
    private boolean wasSuccess;

    /**
     * Default constructor.
     */
    public FridgeResponseMessage() {
    }

    public FridgeDTO getFridgeContent() {
        return fridgeContent;
    }

    /**
     * Sets the fridge content.
     *
     * @param fridgeContent the new fridge content
     */
    public void setFridgeContent(FridgeDTO fridgeContent) {
        this.fridgeContent = fridgeContent;
    }

    /**
     * Checks if the operation was successful.
     *
     * @return true if the operation was successful, false otherwise
     */
    public boolean isWasSuccess() {
        return wasSuccess;
    }

    /**
     * Sets the success status of the operation.
     *
     * @param wasSuccess the new success status
     */
    public void setWasSuccess(boolean wasSuccess) {
        this.wasSuccess = wasSuccess;
    }
}