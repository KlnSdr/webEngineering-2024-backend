package com.sks.demo.api;

public interface DemoSender {
    DemoResponseMessage sendRequest(DemoRequestMessage message);
    void sendResponse(DemoRequestMessage request, DemoResponseMessage response);
}
