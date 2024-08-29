package com.sks.gateway.products.rest;

import com.sks.gateway.common.MessageErrorHandler;
import com.sks.products.api.ProductDTO;
import com.sks.products.api.ProductsRequestMessage;
import com.sks.products.api.ProductsResponseMessage;
import com.sks.products.api.ProductsSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductsResourceTest {
    @Mock
    private ProductsSender sender;
    @Mock
    private MessageErrorHandler errorHandler;

    private ProductsResource controller;

    @BeforeEach
    public void setUp() {
        sender = mock(ProductsSender.class);
        errorHandler = mock(MessageErrorHandler.class);
        controller = new ProductsResource(sender, errorHandler);
    }

    @Test
    public void testGetProductById_Success() {
        ProductDTO product = new ProductDTO(1, "Democracy", "t");
        ProductsResponseMessage responseMessage = new ProductsResponseMessage(new ProductDTO[] {product});
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
    public void testGetProductById_Throws500OnMessageError() {
        ProductsResponseMessage responseMessage = new ProductsResponseMessage();
        responseMessage.setDidError(true);
        when(sender.sendRequest(any(ProductsRequestMessage.class))).thenReturn(responseMessage);
        doThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error")).when(errorHandler).handle(responseMessage);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.getProductById(1);
        });

        assertEquals("Internal Server Error", exception.getReason());
    }

    @Test
    public void testGetMultiple_Success() {
        ProductDTO product1 = new ProductDTO(1, "Deutsch", "Tüten");
        ProductDTO product2 = new ProductDTO(2, "Mitleid", "Dosen");
        ProductsResponseMessage responseMessage = new ProductsResponseMessage(new ProductDTO[] {product1, product2});

        when(sender.sendRequest(any(ProductsRequestMessage.class))).thenReturn(responseMessage);

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

        assertEquals("Products not found", exception.getReason());
    }

    @Test
    public void testGetMultiple_Throws500OnMessageError() {
        ProductsResponseMessage responseMessage = new ProductsResponseMessage();
        responseMessage.setDidError(true);
        when(sender.sendRequest(any(ProductsRequestMessage.class))).thenReturn(responseMessage);
        doThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error")).when(errorHandler).handle(responseMessage);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.getMultiple(new long[] {1, 2});
        });

        assertEquals("Internal Server Error", exception.getReason());
    }

    @Test
    public void testGetAll() {
        ProductDTO product1 = new ProductDTO(1, "Freedom", "baldEagle per oil barrel");
        ProductDTO product2 = new ProductDTO(2, "Freedom of press", "Journalists per prison cell");
        ProductsResponseMessage responseMessage = new ProductsResponseMessage(new ProductDTO[] {product1, product2});

        when(sender.sendRequest(any(ProductsRequestMessage.class))).thenReturn(responseMessage);

        ProductDTO[] results = controller.getAllProducts();

        assertNotNull(results);
        assertEquals(2, results.length);
        assertEquals(product1.getId(), results[0].getId());
        assertEquals(product2.getId(), results[1].getId());
    }

    @Test
    public void testGetAll_Throws500OnMessageError() {
        ProductsResponseMessage responseMessage = new ProductsResponseMessage();
        responseMessage.setDidError(true);
        when(sender.sendRequest(any(ProductsRequestMessage.class))).thenReturn(responseMessage);
        doThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error")).when(errorHandler).handle(responseMessage);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.getAllProducts();
        });

        assertEquals("Internal Server Error", exception.getReason());
    }
}
