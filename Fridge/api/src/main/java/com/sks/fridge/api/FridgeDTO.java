package com.sks.fridge.api;

import java.util.Map;

/**
 * Data Transfer Object for the fridge.
 */
public class FridgeDTO {
    private String userUri;
    private Map<String, Integer> products;

    /**
     * Gets the URI of the user.
     *
     * @return the user URI
     */
    public String getUserUri() {
        return userUri;
    }

    /**
     * Sets the URI of the user.
     *
     * @param userUri the new user URI
     */
    public void setUserUri(String userUri) {
        this.userUri = userUri;
    }

    /**
     * Gets the products in the fridge.
     *
     * @return a map of product names to their quantities
     */
    public Map<String, Integer> getProducts() {
        return products;
    }

    /**
     * Sets the products in the fridge.
     *
     * @param products a map of product names to their quantities
     */
    public void setProducts(Map<String, Integer> products) {
        this.products = products;
    }
}