package com.sks.products.service;

import com.sks.products.api.ProductDTO;
import com.sks.products.api.ProductsRequestMessage;
import com.sks.products.api.ProductsSender;
import com.sks.products.service.data.ProductEntity;
import com.sks.products.service.data.ProductsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ListenerTest {
    @Mock
    private ProductsService productsService;

    @Mock
    private ProductsSender sender;

    private Listener listener;

    @BeforeEach
    public void setUp() {
        productsService = mock(ProductsService.class);
        sender = mock(ProductsSender.class);
        listener = new Listener(sender, productsService);
    }

    @Test
    public void listenSendsAllProductsWhenRequestTypeIsAll() {
        final ProductsRequestMessage message = new ProductsRequestMessage();

        ProductEntity product = new ProductEntity("Product1", "Unit1");
        product.setId(1L);
        when(productsService.getAll()).thenReturn(List.of(product));

        listener.listen(message);

        ProductDTO[] expectedPayload = {new ProductDTO(1L, "Product1", "Unit1")};
        verify(sender).sendResponse(eq(message), argThat(response -> {
            ProductDTO[] payload = response.getProducts();
            return payload.length == 1 && equals(expectedPayload, payload);
        }));
    }

    @Test
    public void listenSendsProductsWhenRequestTypeIsFiltered() {
        final ProductsRequestMessage message = new ProductsRequestMessage(new long[]{1L});

        ProductEntity product = new ProductEntity("Product1", "Unit1");
        product.setId(1L);
        when(productsService.find(any(Long.class))).thenReturn(Optional.of(product));

        listener.listen(message);

        ProductDTO[] expectedPayload = {new ProductDTO(1L, "Product1", "Unit1")};
        verify(sender).sendResponse(eq(message), argThat(response -> {
            ProductDTO[] payload = response.getProducts();
            return equals(expectedPayload, payload);
        }));
    }

    @Test
    public void listenSendsProductsWhenRequestTypeIsFiltered_notFound() {
        final ProductsRequestMessage message = new ProductsRequestMessage(new long[]{1L});

        when(productsService.find(any(Long.class))).thenReturn(Optional.empty());

        listener.listen(message);

        ProductDTO[] expectedPayload = {};
        verify(sender).sendResponse(eq(message), argThat(response -> {
            ProductDTO[] payload = response.getProducts();
            return equals(expectedPayload, payload);
        }));
    }

    private boolean equals(ProductDTO[] expectedPayload, ProductDTO[] payload) {
        if (expectedPayload.length != payload.length) {
            return false;
        }
        for (int i = 0; i < expectedPayload.length; i++) {
            if (expectedPayload[i].getId() != payload[i].getId() || !expectedPayload[i].getName().equals(payload[i].getName()) || !expectedPayload[i].getUnit().equals(payload[i].getUnit())) {
                return false;
            }
        }
        return true;
    }
}
