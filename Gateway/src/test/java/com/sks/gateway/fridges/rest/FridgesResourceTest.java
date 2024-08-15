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
    
}
