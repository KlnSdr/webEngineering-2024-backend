package com.sks.base.api;

public interface BaseSender<I extends BaseMessage, O extends BaseMessage> {
    O sendRequest(I message);
    void sendResponse(I request, O response);
}
