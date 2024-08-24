package com.sks.fridge.service;

import com.sks.fridge.api.*;
import com.sks.fridge.service.data.entity.FridgeEntity;
import com.sks.fridge.service.data.service.FridgeService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class Listener implements FridgeListener {
    private final FridgeSender sender;
    private final FridgeService service;

    public Listener(FridgeSender sender, FridgeService service) {
        this.sender = sender;
        this.service = service;
    }

    @Override
    public void listen(FridgeRequestMessage message) {
        final FridgeResponseMessage response = switch (message.getRequestType()) {
            case GET -> handleGet(message);
            case UPDATE -> handleUpdate(message);
            case DELETE -> handleDelete(message);
        };
        sender.sendResponse(message, response);
    }

    private FridgeResponseMessage handleGet(FridgeRequestMessage request) {
        final long userId = request.getUserId();
        final String userUri = uriFromUserId(userId);
        final Optional<FridgeEntity> fridge = service.findByUserUri(userUri);
        final FridgeResponseMessage response = new FridgeResponseMessage();

        if (fridge.isEmpty()) {
            final FridgeDTO emptyFridge = new FridgeDTO();
            emptyFridge.setUserUri(userUri);
            emptyFridge.setProducts(new HashMap<>());
            response.setFridgeContent(emptyFridge);
            return response;
        }

        response.setFridgeContent(map(fridge.get()));
        return response;
    }

    private FridgeResponseMessage handleUpdate(FridgeRequestMessage request) {
        final long userId = request.getUserId();
        final String userUri = uriFromUserId(userId);
        final Optional<FridgeEntity> fridge = service.findByUserUri(userUri);
        final FridgeResponseMessage response = new FridgeResponseMessage();
        response.setWasSuccess(true);

        final FridgeEntity entity;
        if (fridge.isEmpty()) {
            entity = new FridgeEntity();
            entity.setUserUri(userUri);
            entity.setProductQuantityMap(map(request.getProducts()));
        } else {
            entity = fridge.get();
            final Map<String, Integer> products = entity.getProductQuantityMap();
            for (FridgeAddItemDTO product : request.getProducts()) {
                products.put(uriFromProductId(product.getID()), product.getQuantity());
            }
        }
        try {
            final FridgeEntity savedFridge = service.save(entity);
            response.setFridgeContent(map(savedFridge));
        } catch (Exception e) {
            response.setWasSuccess(false);
            response.setFridgeContent(map(entity));
        }
        return response;
    }

    private FridgeResponseMessage handleDelete(FridgeRequestMessage request) {
        final long userId = request.getUserId();
        final long productId = request.getProductId();
        final String productUri = uriFromProductId(productId);

        final Optional<FridgeEntity> fridge = service.findByUserUri(uriFromUserId(userId));
        final FridgeResponseMessage response = new FridgeResponseMessage();
        boolean wasSuccess = true;

        if (fridge.isPresent()) {
            final FridgeEntity entity = fridge.get();
            final Map<String, Integer> products = entity.getProductQuantityMap();
            products.remove(productUri);
            entity.setProductQuantityMap(products);
            try {
                service.save(entity);
            } catch (Exception e) {
                wasSuccess = false;
            }
        }

        response.setWasSuccess(wasSuccess);
        return response;
    }

    private String uriFromUserId(long id) {
        return "/users/id/" + id;
    }

    private String uriFromProductId(long id) {
        return "/products/" + id;
    }

    private FridgeDTO map(FridgeEntity fridgeEntity) {
        final FridgeDTO dto = new FridgeDTO();
        dto.setUserUri(fridgeEntity.getUserUri());
        dto.setProducts(fridgeEntity.getProductQuantityMap());
        return dto;
    }

    private Map<String, Integer> map(List<FridgeAddItemDTO> products) {
        final Map<String, Integer> map = new HashMap<>();
        for (FridgeAddItemDTO product : products) {
            map.put(uriFromProductId(product.getID()), product.getQuantity());
        }
        return map;
    }
}
