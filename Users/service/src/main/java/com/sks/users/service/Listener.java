package com.sks.users.service;

import com.sks.users.api.*;
import com.sks.users.service.data.TokenEntity;
import com.sks.users.service.data.TokenService;
import com.sks.users.service.data.UsersEntity;
import com.sks.users.service.data.UsersService;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class Listener implements UsersListener {
    private final UsersService service;
    private final TokenService tokenService;
    private final UsersSender sender;

    public Listener(UsersSender sender, UsersService service, TokenService tokenService) {
        this.sender = sender;
        this.service = service;
        this.tokenService = tokenService;
    }

    @Override
    public void listen(UsersRequestMessage message) {
        UsersResponseMessage response;
        try {
            response = switch (message.getRequestType()) {
                case GET_BY_ID -> handleGetById(message);
                case GET_BY_IDP -> handleGetByIdp(message);
                case CREATE -> handleCreate(message);
                case STORE_TOKEN -> handleStoreToken(message);
                case IS_KNOWN_TOKEN -> handleIsKnownToken(message);
            };
        } catch (Exception e) {
            response = buildErrorResponse(e);
        }
        sender.sendResponse(message, response);
    }

    private UsersResponseMessage buildErrorResponse(Exception e) {
        UsersResponseMessage response = new UsersResponseMessage();
        response.setDidError(true);
        response.setErrorMessage("Error while processing message");
        response.setException(e);
        return response;
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

    private UsersResponseMessage handleStoreToken(UsersRequestMessage message) {
        final String token = message.getToken();
        final TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setToken(token);
        tokenEntity.setValidTill(message.getValidTill());
        tokenService.save(tokenEntity);

        return new UsersResponseMessage();
    }

    private UsersResponseMessage handleIsKnownToken(UsersRequestMessage message) {
        final boolean isKnown = tokenService.isKnownToken(message.getToken());
        final UsersResponseMessage response = new UsersResponseMessage();
        response.setKnownToken(isKnown);
        return response;
    }

    private UserDTO map(UsersEntity user) {
        final UserDTO dto = new UserDTO();
        dto.setUserId(user.getId());
        dto.setUserName(user.getDisplayName());
        return dto;
    }
}
