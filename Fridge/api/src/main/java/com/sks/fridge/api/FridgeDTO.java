package com.sks.fridge.api;

import java.util.Map;

public class FridgeDTO {
    private String userUri;
    private Map<String, Integer> products;

    public String getUserUri() {
        return userUri;
    }

    public void setUserUri(String userUri) {
        this.userUri = userUri;
    }

    public Map<String, Integer> getProducts() {
        return products;
    }

    public void setProducts(Map<String, Integer> products) {
        this.products = products;
    }
}
