package com.sks.products.api;

import com.sks.base.api.BaseMessage;

public class ProductsRequestMessage extends BaseMessage {
    private long productId;

    public ProductsRequestMessage() {
    }

    public ProductsRequestMessage(long productId) {
        this.productId = productId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }
}
