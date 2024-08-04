package com.sks.recipes.api;

import com.sks.base.api.BaseMessage;

public class RecipeResponseMessage extends BaseMessage {
    private String message;

    public RecipeResponseMessage() {
    }

    public RecipeResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
