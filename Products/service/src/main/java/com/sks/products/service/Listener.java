package com.sks.products.service;

import com.sks.products.api.*;
import com.sks.products.service.data.ProductEntity;
import com.sks.products.service.data.ProductsService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
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
        final List<ProductDTO> products = new ArrayList<>();

        for (long id : message.getProductId()) {
            final Optional<ProductEntity> product = service.find(id);
            product.ifPresent(entity -> products.add(map(entity)));
        }

        sender.sendResponse(message, new ProductsResponseMessage(products.toArray(new ProductDTO[0])));
    }

    private ProductDTO map(ProductEntity entity) {
        return new ProductDTO(entity.getId(), entity.getName(), entity.getUnit());
    }
}
