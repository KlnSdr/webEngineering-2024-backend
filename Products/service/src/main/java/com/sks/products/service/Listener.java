package com.sks.products.service;

import com.sks.products.api.ProductsListener;
import com.sks.products.api.ProductsRequestMessage;
import com.sks.products.api.ProductsResponseMessage;
import com.sks.products.api.ProductsSender;
import com.sks.products.service.data.ProductsRepository;
import org.springframework.stereotype.Component;

@Component
public class Listener implements ProductsListener {
    private final ProductsSender sender;
    private final ProductsRepository repo;

    public Listener(ProductsSender sender, ProductsRepository repo) {
        this.sender = sender;
        this.repo = repo;
    }

    @Override
    public void listen(ProductsRequestMessage message) {
        System.out.println("Listener got message: " + message.getMessage());
        repo.findAll().forEach(productsEntity -> System.out.println(productsEntity.getName()));
        sender.sendResponse(message, new ProductsResponseMessage("Listener got message: " + message.getMessage()));
    }
}
