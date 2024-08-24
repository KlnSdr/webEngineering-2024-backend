package com.sks.gateway.fridges.rest;

import com.sks.fridge.api.*;
import com.sks.gateway.fridges.dto.FridgeItemDTO;
import com.sks.products.api.ProductDTO;
import com.sks.products.api.ProductsRequestMessage;
import com.sks.products.api.ProductsResponseMessage;
import com.sks.products.api.ProductsSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FridgesResourceTest {

    @Mock
    private ProductsSender productsSender;
    @Mock
    private FridgeSender fridgeSender;
    private FridgesResource controller;

    @BeforeEach
    public void setUp() {
        productsSender = mock(ProductsSender.class);
        fridgeSender = mock(FridgeSender.class);
        controller = new FridgesResource(productsSender, fridgeSender);
    }

    @Test
    void getFridgeItemsReturnsItemsWhenUserIdIsValid() {
        long userId = 1L;
        FridgeResponseMessage fridgeResponse = mock(FridgeResponseMessage.class);
        FridgeDTO fridge = mock(FridgeDTO.class);
        when(fridgeSender.sendRequest(any(FridgeRequestMessage.class))).thenReturn(fridgeResponse);
        when(fridgeResponse.getFridgeContent()).thenReturn(fridge);
        when(fridge.getProducts()).thenReturn(Map.of("/products/1", 2));
        ProductsResponseMessage productsResponse = mock(ProductsResponseMessage.class);
        ProductDTO product = new ProductDTO(1L, "Milk", "Litre");
        when(productsSender.sendRequest(any(ProductsRequestMessage.class))).thenReturn(productsResponse);
        when(productsResponse.getProducts()).thenReturn(new ProductDTO[]{product});

        List<FridgeItemDTO> result = controller.getFridgeItems(userId);

        assertEquals(1, result.size());
        assertEquals("Milk", result.getFirst().getName());
    }

    @Test
    void addOrUpdateFridgeItemsReturnsUpdatedItemsWhenRequestIsValid() {
        long userId = 1L;
        List<FridgeAddItemDTO> items = List.of(new FridgeAddItemDTO(1L, 2));
        FridgeResponseMessage fridgeResponse = mock(FridgeResponseMessage.class);
        FridgeDTO fridge = mock(FridgeDTO.class);
        when(fridgeSender.sendRequest(any(FridgeRequestMessage.class))).thenReturn(fridgeResponse);
        when(fridgeResponse.isWasSuccess()).thenReturn(true);
        when(fridgeResponse.getFridgeContent()).thenReturn(fridge);
        when(fridge.getProducts()).thenReturn(Map.of("/products/1", 2));
        ProductsResponseMessage productsResponse = mock(ProductsResponseMessage.class);
        ProductDTO product = new ProductDTO(1L, "Milk", "Litre");
        when(productsSender.sendRequest(any(ProductsRequestMessage.class))).thenReturn(productsResponse);
        when(productsResponse.getProducts()).thenReturn(new ProductDTO[]{product});

        List<FridgeItemDTO> result = controller.addOrUpdateFridgeItems(userId, items);

        assertEquals(1, result.size());
        assertEquals("Milk", result.getFirst().getName());
    }

    @Test
    void addOrUpdateFridgeItemsThrowsExceptionWhenResponseIsNotSuccessful() {
        long userId = 1L;
        List<FridgeAddItemDTO> items = List.of(new FridgeAddItemDTO(1L, 2));
        FridgeResponseMessage fridgeResponse = mock(FridgeResponseMessage.class);
        when(fridgeSender.sendRequest(any(FridgeRequestMessage.class))).thenReturn(fridgeResponse);
        when(fridgeResponse.isWasSuccess()).thenReturn(false);
        when(fridgeResponse.getMessage()).thenReturn("Error");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.addOrUpdateFridgeItems(userId, items);
        });

        assertEquals("Error", exception.getReason());
    }

    @Test
    void deleteFridgeItemReturnsNoContentWhenRequestIsValid() {
        long userId = 1L;
        long productId = 1L;
        FridgeResponseMessage fridgeResponse = mock(FridgeResponseMessage.class);
        when(fridgeSender.sendRequest(any(FridgeRequestMessage.class))).thenReturn(fridgeResponse);
        when(fridgeResponse.isWasSuccess()).thenReturn(true);

        ResponseEntity<Void> response = controller.deleteFridgeItem(userId, productId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteFridgeItemThrowsExceptionWhenResponseIsNotSuccessful() {
        long userId = 1L;
        long productId = 1L;
        FridgeResponseMessage fridgeResponse = mock(FridgeResponseMessage.class);
        when(fridgeSender.sendRequest(any(FridgeRequestMessage.class))).thenReturn(fridgeResponse);
        when(fridgeResponse.isWasSuccess()).thenReturn(false);
        when(fridgeResponse.getMessage()).thenReturn("Error");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.deleteFridgeItem(userId, productId);
        });

        assertEquals("Error", exception.getReason());
    }
}
