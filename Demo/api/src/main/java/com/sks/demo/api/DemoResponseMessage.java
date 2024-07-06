package com.sks.demo.api;

import com.sks.base.api.BaseMessage;

public class DemoResponseMessage extends BaseMessage {
    private String message;

    public DemoResponseMessage() {
    }

    public DemoResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
