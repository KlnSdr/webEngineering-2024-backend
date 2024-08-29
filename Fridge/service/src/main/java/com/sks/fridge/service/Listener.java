package com.sks.fridge.service;

import com.sks.fridge.api.*;
import com.sks.fridge.service.data.entity.FridgeEntity;
import com.sks.fridge.service.data.service.FridgeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class Listener implements FridgeListener {
    private static final Logger log = LoggerFactory.getLogger(Listener.class);
    private final FridgeSender sender;
    private final FridgeService service;
    private final Jackson2JsonMessageConverter converter;

    public Listener(FridgeSender sender, FridgeService service, @Qualifier("fridgeMessageConverter") Jackson2JsonMessageConverter converter) {
        this.sender = sender;
        this.service = service;
        this.converter = converter;
    }

    @Override
    public void listen(Message message) {
        final Object obj = converter.fromMessage(message);
        if (!(obj instanceof FridgeRequestMessage request)) {
            // can't send a response if the message is not a base message, because we don't know the correlation ID
            log.error("Received message is not an instance of FridgeRequestMessage");
            log.error("Received message: {}", obj);
            return;
        }

        FridgeResponseMessage response;
        try {
            response = switch (request.getRequestType()) {
                case GET -> handleGet(request);
                case UPDATE -> handleUpdate(request);
                case DELETE -> handleDelete(request);
            };
        } catch (Exception e) {
            log.error("Error while processing message", e);
            response = buildErrorResponse(e);
        }
        sender.sendResponse(request, response);
    }

    private FridgeResponseMessage buildErrorResponse(Exception e) {
        final FridgeResponseMessage response = new FridgeResponseMessage();
        response.setDidError(true);
        response.setErrorMessage("Error while processing message");
        response.setException(e);
        return response;
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
                products.put(uriFromProductId(product.getProductID()), product.getQuantity());
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
            map.put(uriFromProductId(product.getProductID()), product.getQuantity());
        }
        return map;
    }
}
