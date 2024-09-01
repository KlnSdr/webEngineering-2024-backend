package com.sks.base.api.exceptions;

public class GatewayTimeoutException extends RuntimeException {
    public GatewayTimeoutException(String message) {
        super(message);
    }
}
