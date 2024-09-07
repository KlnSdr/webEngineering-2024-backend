package com.sks.users.api;

import com.sks.base.api.BaseMessage;

public class UsersResponseMessage extends BaseMessage {
    UserDTO user = null;
    private boolean isKnownToken;

    public UsersResponseMessage() {
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public boolean isKnownToken() {
        return isKnownToken;
    }

    public void setKnownToken(boolean isKnownToken) {
        this.isKnownToken = isKnownToken;
    }
}
