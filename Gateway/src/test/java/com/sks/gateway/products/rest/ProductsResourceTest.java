package com.sks.gateway.products.rest;

import com.sks.products.api.ProductDTO;
import com.sks.products.api.ProductsRequestMessage;
import com.sks.products.api.ProductsResponseMessage;
import com.sks.products.api.ProductsSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProductsResourceTest {
    @Mock
    private ProductsSender sender;
    private ProductsResource controller;

    @BeforeEach
    public void setUp() {
        sender = mock(ProductsSender.class);
        controller = new ProductsResource(sender);
    }

    @Test
    public void testGetProductById_Success() {
        ProductDTO product = new ProductDTO(1, "Democracy", "t"); // Assume proper initialization
        ProductsResponseMessage responseMessage = new ProductsResponseMessage(product);
        when(sender.sendRequest(any(ProductsRequestMessage.class))).thenReturn(responseMessage);

        ProductDTO result = controller.getProductById(1);

        assertNotNull(result);
        assertEquals(product.getId(), result.getId());
    }

    @Test
    public void testGetProductById_NotFound() {
        ProductsResponseMessage responseMessage = new ProductsResponseMessage(null);
        when(sender.sendRequest(any(ProductsRequestMessage.class))).thenReturn(responseMessage);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.getProductById(1);
        });

        assertEquals("Product with id 1 not found", exception.getReason());
    }

    @Test
    public void testGetMultiple_Success() {
        ProductDTO product1 = new ProductDTO(1, "", ""); // Assume proper initialization
        ProductDTO product2 = new ProductDTO(2, "", ""); // Assume proper initialization
        ProductsResponseMessage responseMessage1 = new ProductsResponseMessage(product1);
        ProductsResponseMessage responseMessage2 = new ProductsResponseMessage(product2);

        when(sender.sendRequest(any(ProductsRequestMessage.class))).thenReturn(responseMessage1).thenReturn(responseMessage2);

        long[] ids = {1, 2};
        ProductDTO[] results = controller.getMultiple(ids);

        assertNotNull(results);
        assertEquals(2, results.length);
        assertEquals(product1.getId(), results[0].getId());
        assertEquals(product2.getId(), results[1].getId());
    }

    @Test
    public void testGetMultiple_NotFound() {
        ProductsResponseMessage responseMessage = new ProductsResponseMessage(null);
        when(sender.sendRequest(any(ProductsRequestMessage.class))).thenReturn(responseMessage);

        long[] ids = {1, 2};

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.getMultiple(ids);
        });

        assertEquals("Product with id + 1 not found", exception.getReason());
    }
}
