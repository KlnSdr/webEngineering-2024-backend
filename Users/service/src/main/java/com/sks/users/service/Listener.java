package com.sks.users.service;

import com.sks.users.api.*;
import com.sks.users.service.data.UsersEntity;
import com.sks.users.service.data.UsersRepository;
import org.springframework.stereotype.Component;

@Component
public class Listener implements UsersListener {
    private final UsersSender sender;
    private final UsersRepository repo;

    public Listener(UsersSender sender, UsersRepository repo) {
        this.sender = sender;
        this.repo = repo;
    }

    @Override
    public void listen(UsersRequestMessage message) {
        final UsersEntity entity = new UsersEntity();
        entity.setName(message.getMessage());
        repo.save(entity);

        repo.findAll().forEach(usersEntity -> System.out.println(usersEntity.getName()));
        sender.sendResponse(message, new UsersResponseMessage("Listener got message: " + message.getMessage()));
    }
}
