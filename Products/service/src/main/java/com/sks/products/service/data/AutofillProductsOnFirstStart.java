package com.sks.products.service.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AutofillProductsOnFirstStart {
    private static final Logger LOGGER = LoggerFactory.getLogger(AutofillProductsOnFirstStart.class);
    private final ProductsService productsService;

    public AutofillProductsOnFirstStart(ProductsService productsService) {
        this.productsService = productsService;
    }

    public void run() {
        if (checkIfDatabaseIsEmpty()) {
            LOGGER.info("Database is empty, filling with default products...");
            fillDatabase();
            LOGGER.info("done!");
        }
    }

    private boolean checkIfDatabaseIsEmpty() {
        return productsService.getAll().isEmpty();
    }

    private void fillDatabase() {
        final ProductEntity[] products = {
                new ProductEntity("Apple", "kg"),
                new ProductEntity("Banana", "kg"),
                new ProductEntity("Orange", "kg"),
                new ProductEntity("Milk", "l"),
                new ProductEntity("Bread", "pcs"),
                new ProductEntity("Butter", "pcs"),
                new ProductEntity("Cheese", "pcs"),
                new ProductEntity("Eggs", "pcs"),
                new ProductEntity("Sugar", "kg"),
                new ProductEntity("Salt", "kg"),
                new ProductEntity("Pepper", "kg"),
                new ProductEntity("Pasta", "kg"),
                new ProductEntity("Rice", "kg"),
                new ProductEntity("Potatoes", "kg"),
        };

        productsService.saveAll(List.of(products));
    }
}
