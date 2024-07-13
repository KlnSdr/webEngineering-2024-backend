package com.sks.base.api;

public interface BaseQueueConfig {
    String getRequestQueueName();
    String getResponseQueueName();
    String getExchangeName();
    String getRequestRoutingKey();
}
