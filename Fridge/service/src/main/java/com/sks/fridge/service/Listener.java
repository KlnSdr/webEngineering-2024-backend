package com.sks.fridge.service;

import com.sks.fridge.api.*;
import com.sks.fridge.service.data.FridgeEntity;
import com.sks.fridge.service.data.FridgeRepository;
import org.springframework.stereotype.Component;

@Component
public class Listener implements FridgeListener {
    private final FridgeSender sender;
    private final FridgeRepository repo;

    public Listener(FridgeSender sender, FridgeRepository repo) {
        this.sender = sender;
        this.repo = repo;
    }

    @Override
    public void listen(FridgeRequestMessage message) {
        final FridgeEntity entity = new FridgeEntity();
        entity.setName(message.getMessage());
        repo.save(entity);

        repo.findAll().forEach(fridgeEntity -> System.out.println(fridgeEntity.getName()));
        sender.sendResponse(message, new FridgeResponseMessage("Listener got message: " + message.getMessage()));
    }
}
