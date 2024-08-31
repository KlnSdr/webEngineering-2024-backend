package com.sks.products.api;

import com.sks.base.api.BaseSender;

/**
 * Interface for sending product-related messages.
 * Extends the BaseSender interface with specific types for product requests and responses.
 */
public interface ProductsSender extends BaseSender<ProductsRequestMessage, ProductsResponseMessage> {

    /**
     * Sends a product request message and returns the response.
     *
     * @param message the product request message
     * @return the product response message
     */
    ProductsResponseMessage sendRequest(ProductsRequestMessage message);

    /**
     * Sends a product response message based on the request.
     *
     * @param request the product request message
     * @param response the product response message
     */
    void sendResponse(ProductsRequestMessage request, ProductsResponseMessage response);
}