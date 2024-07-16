package com.sks.products.api;

import com.sks.base.api.BaseMessage;

public class ProductsResponseMessage extends BaseMessage {
    private String message;

    public ProductsResponseMessage() {
    }

    public ProductsResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
