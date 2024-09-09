package com.sks.users.api;

import com.sks.base.api.BaseMessage;

import java.util.Date;

/**
 * Represents a request message for user-related operations.
 * This class extends the BaseMessage class and includes additional fields
 * specific to user requests.
 */
public class UsersRequestMessage extends BaseMessage {
    private UsersRequestType requestType;
    private Long userId;
    private String userName;
    private Long idpUserId;
    private String token;
    private Date validTill;

    /**
     * Default constructor.
     */
    public UsersRequestMessage() {
    }

    /**
     * Creates a UsersRequestMessage to find a user by their ID.
     *
     * @param userId the ID of the user to find
     * @return a UsersRequestMessage configured to find a user by ID
     */
    public static UsersRequestMessage findUser(Long userId) {
        UsersRequestMessage message = new UsersRequestMessage();
        message.setRequestType(UsersRequestType.GET_BY_ID);
        message.setUserId(userId);
        return message;
    }

    /**
     * Creates a UsersRequestMessage to find a user by their IDP user ID.
     *
     * @param userId the IDP user ID of the user to find
     * @return a UsersRequestMessage configured to find a user by IDP user ID
     */
    public static UsersRequestMessage findUserIdp(Long userId) {
        UsersRequestMessage message = new UsersRequestMessage();
        message.setRequestType(UsersRequestType.GET_BY_IDP);
        message.setIdpUserId(userId);
        return message;
    }

    /**
     * Creates a UsersRequestMessage to create a new user.
     *
     * @param userName the name of the user to create
     * @param idpUserId the IDP user ID of the user to create
     * @return a UsersRequestMessage configured to create a new user
     */
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

    /**
     * Gets the IDP user ID.
     *
     * @return the IDP user ID
     */
    public Long getIdpUserId() {
        return idpUserId;
    }

    /**
     * Sets the IDP user ID.
     *
     * @param idpUserId the IDP user ID to set
     */
    public void setIdpUserId(Long idpUserId) {
        this.idpUserId = idpUserId;
    }

    /**
     * Gets the request type.
     *
     * @return the request type
     */
    public UsersRequestType getRequestType() {
        return requestType;
    }

    /**
     * Sets the request type.
     *
     * @param requestType the request type to set
     */
    public void setRequestType(UsersRequestType requestType) {
        this.requestType = requestType;
    }

    /**
     * Gets the user ID.
     *
     * @return the user ID
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Sets the user ID.
     *
     * @param userId the user ID to set
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * Gets the user name.
     *
     * @return the user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the user name.
     *
     * @param userName the user name to set
     */
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