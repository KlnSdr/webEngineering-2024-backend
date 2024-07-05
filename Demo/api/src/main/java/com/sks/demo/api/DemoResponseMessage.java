package com.sks.demo.api;

import java.io.Serializable;

public class DemoResponseMessage implements Serializable {
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
