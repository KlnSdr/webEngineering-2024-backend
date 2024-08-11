package com.sks.users.service;

import com.sks.users.api.UsersListener;
import com.sks.users.api.UsersRequestMessage;
import com.sks.users.api.UsersResponseMessage;
import com.sks.users.api.UsersSender;
import org.springframework.stereotype.Component;

@Component
public class Listener implements UsersListener {
    private final UsersSender sender;

    public Listener(UsersSender sender) {
        this.sender = sender;
    }

    @Override
    public void listen(UsersRequestMessage message) {
        sender.sendResponse(message, new UsersResponseMessage("Listener got message: " + message.getMessage()));
    }
}
