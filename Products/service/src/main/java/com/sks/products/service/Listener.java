package com.sks.products.service;

import com.sks.products.api.*;
import com.sks.products.service.data.ProductEntity;
import com.sks.products.service.data.ProductsService;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class Listener implements ProductsListener {
    private final ProductsSender sender;
    private final ProductsService service;

    public Listener(ProductsSender sender, ProductsService service) {
        this.sender = sender;
        this.service = service;
    }

    @Override
    public void listen(ProductsRequestMessage message) {
        final Optional<ProductEntity> product = service.find(message.getProductId());

        if (product.isEmpty()) {
            sender.sendResponse(message, new ProductsResponseMessage());
            return;
        }

        sender.sendResponse(message, new ProductsResponseMessage(map(product.get())));
    }

    private ProductDTO map(ProductEntity entity) {
        return new ProductDTO(entity.getId(), entity.getName(), entity.getUnit());
    }
}
