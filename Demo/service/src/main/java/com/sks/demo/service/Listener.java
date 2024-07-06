package com.sks.demo.service;

import com.sks.demo.api.*;
import org.springframework.stereotype.Component;

@Component
public class Listener implements DemoListener {
    private final DemoSender sender;

    public Listener(DemoSender sender) {
        this.sender = sender;
    }

    @Override
    public void listen(DemoRequestMessage baseMessage) {
        // ...
        sender.sendResponse(baseMessage, new DemoResponseMessage("Listener got message: " + baseMessage.getMessage()));
    }
}
