package com.sks.gateway.products.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProductsResourceTest {
    private ProductsResource controller;

    @BeforeEach
    public void setUp() {
        controller = new ProductsResource(null);
    }

}
