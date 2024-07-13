package com.sks.base.api;

import java.io.Serializable;
import java.util.UUID;

/**
 * Represents a base message with a unique correlation ID.
 * This class is used as a foundation for message-based communication,
 * providing a unique identifier for each message instance.
 */
public class BaseMessage implements Serializable {
    private final UUID correlationId;

    public BaseMessage() {
        this.correlationId = UUID.randomUUID();
    }

    public String getCorrelationId() {
        return correlationId.toString();
    }
}
