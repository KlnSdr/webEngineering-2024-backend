package com.sks.products.api;

import com.sks.base.api.BaseMessage;

public class ProductsResponseMessage extends BaseMessage {
    private ProductDTO product;

    public ProductsResponseMessage() {
    }

    public ProductsResponseMessage(ProductDTO product) {
        this.product = product;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }
}
