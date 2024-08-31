package com.sks.users.api;

/**
 * Enum representing the types of user requests.
 */
public enum UsersRequestType {
    /**
     * Request type for getting a user by their ID.
     */
    GET_BY_ID,

    /**
     * Request type for getting a user by their IDP user ID.
     */
    GET_BY_IDP,

    /**
     * Request type for creating a new user.
     */
    CREATE, STORE_TOKEN, IS_KNOWN_TOKEN
}