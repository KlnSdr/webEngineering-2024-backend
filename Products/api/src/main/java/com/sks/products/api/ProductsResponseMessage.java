package com.sks.products.api;

import com.sks.base.api.BaseMessage;

public class ProductsResponseMessage extends BaseMessage {
    private ProductDTO[] products = new ProductDTO[0];

    public ProductsResponseMessage() {
    }

    public ProductsResponseMessage(ProductDTO[] product) {
        this.products = product;
    }

    public ProductDTO[] getProducts() {
        return products;
    }

    public void setProducts(ProductDTO[] products) {
        this.products = products;
    }
}
