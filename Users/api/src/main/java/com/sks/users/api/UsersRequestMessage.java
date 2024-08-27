package com.sks.users.api;

import com.sks.base.api.BaseMessage;

public class UsersRequestMessage extends BaseMessage {
    private UsersRequestType requestType;
    private Long userId;
    private String userName;
    private Long idpUserId;

    public UsersRequestMessage() {
    }

    public static UsersRequestMessage findUser(Long userId) {
        UsersRequestMessage message = new UsersRequestMessage();
        message.setRequestType(UsersRequestType.GET_BY_ID);
        message.setUserId(userId);
        return message;
    }

    public static UsersRequestMessage findUserIdp(Long userId) {
        UsersRequestMessage message = new UsersRequestMessage();
        message.setRequestType(UsersRequestType.GET_BY_IDP);
        message.setIdpUserId(userId);
        return message;
    }

    public static UsersRequestMessage createUser(String userName, Long idpUserId) {
        UsersRequestMessage message = new UsersRequestMessage();
        message.setRequestType(UsersRequestType.CREATE);
        message.setUserName(userName);
        message.setIdpUserId(idpUserId);
        return message;
    }

    public Long getIdpUserId() {
        return idpUserId;
    }

    public void setIdpUserId(Long idpUserId) {
        this.idpUserId = idpUserId;
    }

    public UsersRequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(UsersRequestType requestType) {
        this.requestType = requestType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
