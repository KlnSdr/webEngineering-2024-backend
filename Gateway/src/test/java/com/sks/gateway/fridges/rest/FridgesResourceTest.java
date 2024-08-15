package com.sks.gateway.fridges.rest;

import com.sks.gateway.fridges.dto.FridgeAddItemDTO;
import com.sks.gateway.fridges.dto.FridgeItemDTO;
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

import java.util.List;

public class FridgesResourceTest {

    @Mock
    private ProductsSender sender;
    private FridgesResource controller;

    @BeforeEach
    public void setUp() {
        sender = mock(ProductsSender.class);
        controller = new FridgesResource(sender);
    }

    @Test
    public void testGetFridgeItems_Success() {
        List<FridgeItemDTO> result = controller.getFridgeItems(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Milk", result.get(0).getName());
    }

    @Test
    public void testAddOrUpdateFridgeItems_Success() {
        ProductDTO product1 = new ProductDTO(1, "Milk", "liters");
        ProductDTO product2 = new ProductDTO(2, "Flour", "kg");

        ProductsResponseMessage responseMessage1 = new ProductsResponseMessage(product1);
        ProductsResponseMessage responseMessage2 = new ProductsResponseMessage(product2);

        when(sender.sendRequest(any(ProductsRequestMessage.class)))
                .thenReturn(responseMessage1)
                .thenReturn(responseMessage2);

        List<FridgeAddItemDTO> items = List.of(
                new FridgeAddItemDTO(1, 2.5),
                new FridgeAddItemDTO(2, 3.0)
        );

        List<FridgeItemDTO> results = controller.addOrUpdateFridgeItems(1L, items);

        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals(product1.getId(), results.get(0).getId());
        assertEquals(product2.getId(), results.get(1).getId());
    }

    @Test
    public void testAddOrUpdateFridgeItems_NotFound() {
        ProductsResponseMessage responseMessage = new ProductsResponseMessage(null);
        when(sender.sendRequest(any(ProductsRequestMessage.class))).thenReturn(responseMessage);

        List<FridgeAddItemDTO> items = List.of(new FridgeAddItemDTO(1, 2.5));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.addOrUpdateFridgeItems(1L, items);
        });

        assertEquals("Product with id 1 not found", exception.getReason());
    }

}
