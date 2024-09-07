package com.sks.users.api;

import com.sks.base.api.BaseMessage;

import java.util.Date;

public class UsersRequestMessage extends BaseMessage {
    private UsersRequestType requestType;
    private Long userId;
    private String userName;
    private Long idpUserId;
    private String token;
    private Date validTill;

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

    public static UsersRequestMessage storeToken(String token, Date validTill) {
        UsersRequestMessage message = new UsersRequestMessage();
        message.setRequestType(UsersRequestType.STORE_TOKEN);
        message.setToken(token);
        message.setValidTill(validTill);
        return message;
    }

    public static UsersRequestMessage isKnownToken(String token) {
        UsersRequestMessage message = new UsersRequestMessage();
        message.setRequestType(UsersRequestType.IS_KNOWN_TOKEN);
        message.setToken(token);
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getValidTill() {
        return validTill;
    }

    public void setValidTill(Date validTill) {
        this.validTill = validTill;
    }
}
