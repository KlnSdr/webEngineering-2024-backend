package com.sks.recipes.api;

import com.sks.base.api.BaseMessage;

public class RecipeRequestMessage extends BaseMessage {
    private String message;

    public RecipeRequestMessage() {
    }

    public RecipeRequestMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
