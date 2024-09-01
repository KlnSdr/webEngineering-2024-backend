package com.sks.gateway.fridges.rest;

import com.sks.fridge.api.*;
import com.sks.gateway.common.MessageErrorHandler;
import com.sks.gateway.fridges.dto.FridgeItemDTO;
import com.sks.gateway.util.AccessVerifier;
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
import static org.mockito.Mockito.*;

public class FridgesResourceTest {

    @Mock
    private ProductsSender productsSender;
    @Mock
    private FridgeSender fridgeSender;
    @Mock
    private AccessVerifier accessVerifier;
    @Mock
    private MessageErrorHandler messageErrorHandler;

    private FridgesResource controller;

    @BeforeEach
    public void setUp() {
        productsSender = mock(ProductsSender.class);
        fridgeSender = mock(FridgeSender.class);
        accessVerifier = mock(AccessVerifier.class);
        messageErrorHandler = mock(MessageErrorHandler.class);
        controller = new FridgesResource(productsSender, fridgeSender, accessVerifier, messageErrorHandler);
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
        when(accessVerifier.verifyAccessesSelf(userId, null)).thenReturn(true);

        List<FridgeItemDTO> result = controller.getFridgeItems(userId, null);

        assertEquals(1, result.size());
        assertEquals("Milk", result.getFirst().getName());
    }

    @Test
    void getFridgeItemsReturns500WhenErrorOnMessageSend() {
        final long userId = 1L;
        final FridgeResponseMessage responseMessage = new FridgeResponseMessage();
        responseMessage.setDidError(true);
        when(fridgeSender.sendRequest(any(FridgeRequestMessage.class))).thenReturn(responseMessage);
        when(accessVerifier.verifyAccessesSelf(userId, null)).thenReturn(true);
        doThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error")).when(messageErrorHandler).handle(responseMessage);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.getFridgeItems(userId, null);
        });

        assertEquals("Internal Server Error", exception.getReason());
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
        when(accessVerifier.verifyAccessesSelf(userId, null)).thenReturn(true);

        List<FridgeItemDTO> result = controller.addOrUpdateFridgeItems(userId, items, null);

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
        when(fridgeResponse.getErrorMessage()).thenReturn("Error");
        when(accessVerifier.verifyAccessesSelf(userId, null)).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.addOrUpdateFridgeItems(userId, items, null);
        });

        assertEquals("Error", exception.getReason());
    }

    @Test
    void addOrUpdateFridgeItemsReturns500WhenErrorOnMessageSend() {
        final long userId = 1L;
        final FridgeResponseMessage responseMessage = new FridgeResponseMessage();
        responseMessage.setDidError(true);
        when(fridgeSender.sendRequest(any(FridgeRequestMessage.class))).thenReturn(responseMessage);
        when(accessVerifier.verifyAccessesSelf(userId, null)).thenReturn(true);
        doThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error")).when(messageErrorHandler).handle(responseMessage);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.addOrUpdateFridgeItems(userId, null, null);
        });

        assertEquals("Internal Server Error", exception.getReason());
    }

    @Test
    void deleteFridgeItemReturnsNoContentWhenRequestIsValid() {
        long userId = 1L;
        long productId = 1L;
        FridgeResponseMessage fridgeResponse = mock(FridgeResponseMessage.class);
        when(fridgeSender.sendRequest(any(FridgeRequestMessage.class))).thenReturn(fridgeResponse);
        when(fridgeResponse.isWasSuccess()).thenReturn(true);
        when(accessVerifier.verifyAccessesSelf(userId, null)).thenReturn(true);

        ResponseEntity<Void> response = controller.deleteFridgeItem(userId, productId, null);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteFridgeItemThrowsExceptionWhenResponseIsNotSuccessful() {
        long userId = 1L;
        long productId = 1L;
        FridgeResponseMessage fridgeResponse = mock(FridgeResponseMessage.class);
        when(fridgeSender.sendRequest(any(FridgeRequestMessage.class))).thenReturn(fridgeResponse);
        when(fridgeResponse.isWasSuccess()).thenReturn(false);
        when(fridgeResponse.getErrorMessage()).thenReturn("Error");
        when(accessVerifier.verifyAccessesSelf(userId, null)).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.deleteFridgeItem(userId, productId, null);
        });

        assertEquals("Error", exception.getReason());
    }

    @Test
    void deleteFridgeItemsReturns500WhenErrorOnMessageSend() {
        final long userId = 1L;
        final FridgeResponseMessage responseMessage = new FridgeResponseMessage();
        responseMessage.setDidError(true);
        when(fridgeSender.sendRequest(any(FridgeRequestMessage.class))).thenReturn(responseMessage);
        when(accessVerifier.verifyAccessesSelf(userId, null)).thenReturn(true);
        doThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error")).when(messageErrorHandler).handle(responseMessage);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.deleteFridgeItem(userId, 42L, null);
        });

        assertEquals("Internal Server Error", exception.getReason());
    }
}
