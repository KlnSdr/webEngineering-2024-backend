package com.sks.products.api;

import com.sks.base.api.BaseMessage;

/**
 * Represents a request message for products.
 */
public class ProductsRequestMessage extends BaseMessage {
    private long[] productId = new long[0];
    private ProductsRequestType requestType;

    /**
     * Default constructor for ProductsRequestMessage.
     * Initializes the request type to ALL.
     */
    public ProductsRequestMessage() {
        requestType = ProductsRequestType.ALL;
    }

    /**
     * Parameterized constructor for ProductsRequestMessage.
     * Initializes the product IDs and sets the request type to FILTERED.
     *
     * @param productId an array of product IDs
     */
    public ProductsRequestMessage(long[] productId) {
        this.productId = productId;
        requestType = ProductsRequestType.FILTERED;
    }

    /**
     * Gets the request type.
     *
     * @return the request type
     */
    public ProductsRequestType getRequestType() {
        return requestType;
    }

    /**
     * Sets the request type.
     *
     * @param requestType the new request type
     */
    public void setRequestType(ProductsRequestType requestType) {
        this.requestType = requestType;
    }

    /**
     * Gets the product IDs.
     *
     * @return an array of product IDs
     */
    public long[] getProductId() {
        return productId;
    }

    /**
     * Sets the product IDs.
     *
     * @param productId an array of product IDs
     */
    public void setProductId(long[] productId) {
        this.productId = productId;
    }
}