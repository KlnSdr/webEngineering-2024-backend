package com.sks.users.service;

import com.sks.users.api.*;
import com.sks.users.service.data.UsersEntity;
import com.sks.users.service.data.UsersService;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class Listener implements UsersListener {
    private final UsersService service;
    private final UsersSender sender;

    public Listener(UsersSender sender, UsersService service) {
        this.sender = sender;
        this.service = service;
    }

    @Override
    public void listen(UsersRequestMessage message) {
        final UsersResponseMessage response = switch (message.getRequestType()) {
            case GET_BY_ID -> handleGetById(message);
            case GET_BY_IDP -> handleGetByIdp(message);
            case CREATE -> handleCreate(message);
        };
        sender.sendResponse(message, response);
    }

    private UsersResponseMessage handleGetById(UsersRequestMessage message) {
        final Optional<UsersEntity> user = service.findById(message.getUserId());
        final UsersResponseMessage response = new UsersResponseMessage();
        user.ifPresent(usersEntity -> response.setUser(map(usersEntity)));
        return response;
    }

    private UsersResponseMessage handleGetByIdp(UsersRequestMessage message) {
        final Optional<UsersEntity> user = service.findByIdpHash(service.hashId(message.getIdpUserId()));
        final UsersResponseMessage response = new UsersResponseMessage();
        user.ifPresent(usersEntity -> response.setUser(map(usersEntity)));
        return response;
    }

    private UsersResponseMessage handleCreate(UsersRequestMessage message) {
        final Optional<UsersEntity> user = service.findByIdpHash(service.hashId(message.getIdpUserId()));
        final UsersResponseMessage response = new UsersResponseMessage();
        if (user.isPresent()) {
            response.setUser(map(user.get()));
        } else {
            final UsersEntity newUser = new UsersEntity();
            newUser.setDisplayName(message.getUserName());
            newUser.setIdpHash(service.hashId(message.getIdpUserId()));
            final UsersEntity savedUser = service.save(newUser);
            response.setUser(map(savedUser));
        }
        return response;
    }

    private UserDTO map(UsersEntity user) {
        final UserDTO dto = new UserDTO();
        dto.setUserId(user.getId());
        dto.setUserName(user.getDisplayName());
        return dto;
    }
}
