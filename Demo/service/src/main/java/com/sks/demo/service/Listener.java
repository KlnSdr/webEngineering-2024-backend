package com.sks.demo.service;

import com.sks.demo.api.*;
import com.sks.demo.service.data.DemoEntity;
import com.sks.demo.service.data.DemoRepository;
import org.springframework.stereotype.Component;

@Component
public class Listener implements DemoListener {
    private final DemoSender sender;
    private final DemoRepository repo;

    public Listener(DemoSender sender, DemoRepository repo) {
        this.sender = sender;
        this.repo = repo;
    }

    @Override
    public void listen(DemoRequestMessage message) {
        final DemoEntity entity = new DemoEntity();
        entity.setName(message.getMessage());
        repo.save(entity);

        repo.findAll().forEach(demoEntity -> System.out.println(demoEntity.getName()));
        sender.sendResponse(message, new DemoResponseMessage("Listener got message: " + message.getMessage()));
    }
}
