package com.sks.fridge.service.data.entity;

import jakarta.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
public class FridgeEntity {

    @Id
    @Column(name = "user_uri", nullable = false)
    private String userUri;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "product_quantity_map",
            joinColumns = @JoinColumn(name = "user_uri"))
    @MapKeyColumn(name = "product_uri")
    @Column(name = "quantity")
    private Map<String, Integer> productQuantityMap = new HashMap<>();

    // Getters and Setters
    public String getUserUri() {
        return userUri;
    }

    public void setUserUri(String userUri) {
        this.userUri = userUri;
    }

    public Map<String, Integer> getProductQuantityMap() {
        return productQuantityMap;
    }

    public void setProductQuantityMap(Map<String, Integer> productQuantityMap) {
        this.productQuantityMap = productQuantityMap;
    }
}
