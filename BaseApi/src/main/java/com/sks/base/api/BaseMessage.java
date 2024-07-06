package com.sks.base.api;

import java.io.Serializable;
import java.util.UUID;

public class BaseMessage implements Serializable {
    private final UUID correlationId;

    public BaseMessage() {
        this.correlationId = UUID.randomUUID();
    }

    public String getCorrelationId() {
        return correlationId.toString();
    }
}
