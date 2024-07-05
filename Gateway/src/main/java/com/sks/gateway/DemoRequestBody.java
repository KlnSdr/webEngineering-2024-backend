package com.sks.gateway;

public class DemoRequestBody {
    private String message;

    public DemoRequestBody() {

    }

    public DemoRequestBody(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
