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
    public void listen(DemoRequestMessage message) {
        // ...
        sender.sendResponse(message, new DemoResponseMessage("Listener got message: " + message.getMessage()));
    }
}
