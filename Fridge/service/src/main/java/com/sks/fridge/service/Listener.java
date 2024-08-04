package com.sks.fridge.service;

import com.sks.fridge.api.*;
import org.springframework.stereotype.Component;

@Component
public class Listener implements FridgeListener {
    private final FridgeSender sender;

    public Listener(FridgeSender sender) {
        this.sender = sender;
    }

    @Override
    public void listen(FridgeRequestMessage message) {
        sender.sendResponse(message, new FridgeResponseMessage("Listener got message: " + message.getMessage()));
    }
}
