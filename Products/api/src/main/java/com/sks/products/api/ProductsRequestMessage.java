package com.sks.products.api;

import com.sks.base.api.BaseMessage;

public class ProductsRequestMessage extends BaseMessage {
    private String message;

    public ProductsRequestMessage() {
    }

    public ProductsRequestMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
