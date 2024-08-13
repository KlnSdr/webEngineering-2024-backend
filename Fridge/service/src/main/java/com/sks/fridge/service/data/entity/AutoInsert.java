package com.sks.fridge.service.data.entity;

import com.sks.fridge.service.data.service.FridgeService;
import org.springframework.stereotype.Component;

import java.util.Map;

// TODO: Remove this class
// this is just used to show that the database is working
@Component
public class AutoInsert {
    private final FridgeService fridgeService;

    public AutoInsert(FridgeService fridgeService) {
        this.fridgeService = fridgeService;
    }

    public void run() {
        final FridgeEntity fridgeEntity = new FridgeEntity();
        fridgeEntity.setUserUri("/users/42");
        fridgeEntity.setProductQuantityMap(Map.of("/products/13", 5, "/products/42", 3));

        fridgeService.save(fridgeEntity);

        fridgeService.getAll().forEach(e -> {
            System.out.println("User URI: " + e.getUserUri());
            e.getProductQuantityMap().forEach((k, v) -> System.out.println("Product URI: " + k + ", Quantity: " + v));
        });
    }
}
