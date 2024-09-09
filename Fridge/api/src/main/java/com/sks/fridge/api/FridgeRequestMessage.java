package com.sks.fridge.api;

import com.sks.base.api.BaseMessage;

import java.util.List;

/**
 * Represents a request message for fridge operations.
 */
public class FridgeRequestMessage extends BaseMessage {
    private FridgeRequestType requestType;
    private long userId;
    private long productId;
    private List<FridgeAddItemDTO> products;
    private String message;

    /**
     * Default constructor.
     */
    public FridgeRequestMessage() {
    }

    /**
     * Constructor with message parameter.
     *
     * @param message the message content
     */
    public FridgeRequestMessage(String message) {
        this.message = message;
    }

    /**
     * Creates a request message to get items by user ID.
     *
     * @param userId the user ID
     * @return the request message
     */
    public static FridgeRequestMessage getByUserId(long userId) {
        final FridgeRequestMessage request = new FridgeRequestMessage();
        request.setRequestType(FridgeRequestType.GET);
        request.setUserId(userId);
        return request;
    }

    /**
     * Creates a request message to delete an item by user ID and product ID.
     *
     * @param userId the user ID
     * @param productId the product ID
     * @return the request message
     */
    public static FridgeRequestMessage deleteByUserAndProduct(long userId, long productId) {
        final FridgeRequestMessage request = new FridgeRequestMessage();
        request.setRequestType(FridgeRequestType.DELETE);
        request.setUserId(userId);
        request.setProductId(productId);
        return request;
    }

    /**
     * Creates a request message to update items by user ID.
     *
     * @param userId the user ID
     * @param products the list of products to update
     * @return the request message
     */
    public static FridgeRequestMessage updateByUserId(long userId, List<FridgeAddItemDTO> products) {
        final FridgeRequestMessage request = new FridgeRequestMessage();
        request.setRequestType(FridgeRequestType.UPDATE);
        request.setUserId(userId);
        request.setProducts(products);
        return request;
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
     * @param userId the new user ID
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * Gets the product ID.
     *
     * @return the product ID
     */
    public Long getProductId() {
        return productId;
    }

    /**
     * Sets the product ID.
     *
     * @param productId the new product ID
     */
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    /**
     * Gets the list of products.
     *
     * @return the list of products
     */
    public List<FridgeAddItemDTO> getProducts() {
        return products;
    }

    /**
     * Sets the list of products.
     *
     * @param products the new list of products
     */
    public void setProducts(List<FridgeAddItemDTO> products) {
        this.products = products;
    }

    /**
     * Gets the request type.
     *
     * @return the request type
     */
    public FridgeRequestType getRequestType() {
        return requestType;
    }

    /**
     * Sets the request type.
     *
     * @param requestType the new request type
     */
    public void setRequestType(FridgeRequestType requestType) {
        this.requestType = requestType;
    }

    /**
     * Gets the message content.
     *
     * @return the message content
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message content.
     *
     * @param message the new message content
     */
    public void setMessage(String message) {
        this.message = message;
    }
}