package com.sks.demo.api;

public class DemoRequestMessage extends BaseMessage {
    private String message;

    public DemoRequestMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
