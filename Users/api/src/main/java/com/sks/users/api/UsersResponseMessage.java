package com.sks.users.api;

import com.sks.base.api.BaseMessage;

/**
 * Represents a response message for user-related operations.
 * This class extends the BaseMessage class and includes a UserDTO object.
 */
public class UsersResponseMessage extends BaseMessage {
    private UserDTO user = null;
    private boolean isKnownToken;

    /**
     * Default constructor.
     */
    public UsersResponseMessage() {
    }

    /**
     * Gets the user data transfer object (DTO).
     *
     * @return the user DTO
     */
    public UserDTO getUser() {
        return user;
    }

    /**
     * Sets the user data transfer object (DTO).
     *
     * @param user the user DTO to set
     */
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