package com.sks.fridge.api;

import com.sks.base.api.BaseMessage;

import java.util.List;

public class FridgeRequestMessage extends BaseMessage {
    private FridgeRequestType requestType;
    private long userId;
    private long productId;
    private List<FridgeAddItemDTO> products;
    private String message;

    public FridgeRequestMessage() {
    }

    public FridgeRequestMessage(String message) {
        this.message = message;
    }

    public static FridgeRequestMessage getByUserId(long userId) {
        final FridgeRequestMessage request = new FridgeRequestMessage();
        request.setRequestType(FridgeRequestType.GET);
        request.setUserId(userId);
        return request;
    }

    public static FridgeRequestMessage deleteByUserAndProduct(long userId, long productId) {
        final FridgeRequestMessage request = new FridgeRequestMessage();
        request.setRequestType(FridgeRequestType.DELETE);
        request.setUserId(userId);
        request.setProductId(productId);
        return request;
    }

    public static FridgeRequestMessage updateByUserId(long userId, List<FridgeAddItemDTO> products) {
        final FridgeRequestMessage request = new FridgeRequestMessage();
        request.setRequestType(FridgeRequestType.UPDATE);
        request.setUserId(userId);
        request.setProducts(products);
        return request;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public List<FridgeAddItemDTO> getProducts() {
        return products;
    }

    public void setProducts(List<FridgeAddItemDTO> products) {
        this.products = products;
    }

    public FridgeRequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(FridgeRequestType requestType) {
        this.requestType = requestType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
