package com.sks.fridge.service.data.entity;

import jakarta.persistence.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Entity representing a fridge.
 */
@Entity
public class FridgeEntity {

    /**
     * The unique identifier for the user.
     */
    @Id
    @Column(name = "user_uri", nullable = false)
    private String userUri;

    /**
     * A map storing the quantity of each product in the fridge.
     * The key is the product URI and the value is the quantity.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "product_quantity_map",
            joinColumns = @JoinColumn(name = "user_uri"))
    @MapKeyColumn(name = "product_uri")
    @Column(name = "quantity")
    private Map<String, Integer> productQuantityMap = new HashMap<>();

    /**
     * Gets the user URI.
     *
     * @return the user URI
     */
    public String getUserUri() {
        return userUri;
    }

    /**
     * Sets the user URI.
     *
     * @param userUri the new user URI
     */
    public void setUserUri(String userUri) {
        this.userUri = userUri;
    }

    /**
     * Gets the product quantity map.
     *
     * @return the product quantity map
     */
    public Map<String, Integer> getProductQuantityMap() {
        return productQuantityMap;
    }

    /**
     * Sets the product quantity map.
     *
     * @param productQuantityMap the new product quantity map
     */
    public void setProductQuantityMap(Map<String, Integer> productQuantityMap) {
        this.productQuantityMap = productQuantityMap;
    }
}