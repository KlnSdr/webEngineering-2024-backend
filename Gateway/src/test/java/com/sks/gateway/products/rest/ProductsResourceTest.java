package com.sks.gateway.products.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProductsResourceTest {
    private ProductsResource controller;

    @BeforeEach
    public void setUp() {
        controller = new ProductsResource();
    }

    @Test
    public void testGetProductById() {
        int id = 1;
        ProductResponse response = controller.getProductById(id);

        assertNotNull(response);
        assertEquals(id, response.getId());
        assertEquals("Streuselkäse", response.getName());
        assertEquals("t", response.getUnit());
    }

    @Test
    public void testGetMultiple() {
        int[] ids = {1, 2, 3};
        ProductResponse[] responses = controller.getMultiple(ids);

        assertNotNull(responses);
        assertEquals(ids.length, responses.length);

        for (int i = 0; i < ids.length; i++) {
            assertNotNull(responses[i]);
            assertEquals(ids[i], responses[i].getId());
            assertEquals("Streuselkäse", responses[i].getName());
            assertEquals("t", responses[i].getUnit());
        }
    }
}
