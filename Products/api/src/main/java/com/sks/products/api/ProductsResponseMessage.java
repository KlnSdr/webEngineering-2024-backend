package com.sks.products.api;

import com.sks.base.api.BaseMessage;

/**
 * Represents a response message for products.
 */
public class ProductsResponseMessage extends BaseMessage {
    private ProductDTO[] products = new ProductDTO[0];

    /**
     * Default constructor for ProductsResponseMessage.
     */
    public ProductsResponseMessage() {
    }

    /**
     * Parameterized constructor for ProductsResponseMessage.
     *
     * @param products an array of ProductDTO objects
     */
    public ProductsResponseMessage(ProductDTO[] products) {
        this.products = products;
    }

    /**
     * Gets the array of ProductDTO objects.
     *
     * @return an array of ProductDTO objects
     */
    public ProductDTO[] getProducts() {
        return products;
    }

    /**
     * Sets the array of ProductDTO objects.
     *
     * @param products an array of ProductDTO objects
     */
    public void setProducts(ProductDTO[] products) {
        this.products = products;
    }
}