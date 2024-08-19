package com.sks.users.api;

import com.sks.base.api.BaseMessage;

public class UsersResponseMessage extends BaseMessage {
    UserDTO user = null;

    public UsersResponseMessage() {
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
