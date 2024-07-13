package com.sks.demo.api;

import com.sks.base.api.BaseSender;

public interface DemoSender extends BaseSender<DemoRequestMessage, DemoResponseMessage> {
    DemoResponseMessage sendRequest(DemoRequestMessage message);
    void sendResponse(DemoRequestMessage request, DemoResponseMessage response);
}
