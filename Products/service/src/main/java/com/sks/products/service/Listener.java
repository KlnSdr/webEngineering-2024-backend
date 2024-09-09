package com.sks.products.service;

import com.sks.products.api.*;
import com.sks.products.service.data.ProductEntity;
import com.sks.products.service.data.ProductsService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Listener class for handling product-related messages.
 * Implements the ProductsListener interface to process incoming product requests.
 */
@Component
public class Listener implements ProductsListener {
    private final ProductsSender sender;
    private final ProductsService service;

    /**
     * Constructs a Listener with the specified ProductsSender and ProductsService.
     *
     * @param sender the sender for sending product responses
     * @param service the service for managing product entities
     */
    public Listener(ProductsSender sender, ProductsService service) {
        this.sender = sender;
        this.service = service;
    }

    /**
     * Listens for incoming product request messages and processes them.
     *
     * @param message the product request message
     */
    @Override
    public void listen(ProductsRequestMessage message) {
        ProductsResponseMessage response;
        try {
            final ProductDTO[] payload = switch (message.getRequestType()) {
                case ALL -> getAll();
                case FILTERED -> getFiltered(message.getProductId());
            };
            response = new ProductsResponseMessage(payload);
        } catch (Exception e) {
            response = buildErrorResponse(e);
        }

        sender.sendResponse(message, response);
    }

    private ProductsResponseMessage buildErrorResponse(Exception e) {
        final ProductsResponseMessage response = new ProductsResponseMessage();
        response.setDidError(true);
        response.setErrorMessage("Error while processing message");
        response.setException(e);
        return response;
    }

    /**
     * Retrieves all product entities and maps them to ProductDTO objects.
     *
     * @return an array of ProductDTO objects representing all products
     */
    private ProductDTO[] getAll() {
        return service.getAll().stream().map(this::map).toArray(ProductDTO[]::new);
    }

    /**
     * Retrieves filtered product entities based on the specified IDs and maps them to ProductDTO objects.
     *
     * @param ids an array of product IDs to filter by
     * @return an array of ProductDTO objects representing the filtered products
     */
    private ProductDTO[] getFiltered(long[] ids) {
        final List<ProductDTO> products = new ArrayList<>();

        for (long id : ids) {
            final Optional<ProductEntity> product = service.find(id);
            product.ifPresent(entity -> products.add(map(entity)));
        }

        return products.toArray(new ProductDTO[0]);
    }

    /**
     * Maps a ProductEntity object to a ProductDTO object.
     *
     * @param entity the ProductEntity object to map
     * @return the mapped ProductDTO object
     */
    private ProductDTO map(ProductEntity entity) {
        return new ProductDTO(entity.getId(), entity.getName(), entity.getUnit());
    }
}