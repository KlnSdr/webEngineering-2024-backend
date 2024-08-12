package com.sks.products.api;

import com.sks.base.api.BaseMessage;

public class ProductsRequestMessage extends BaseMessage {
    private long[] productId = new long[0];
    private ProductsRequestType requestType;

    public ProductsRequestMessage() {
        requestType = ProductsRequestType.ALL;
    }

    public ProductsRequestMessage(long[] productId) {
        this.productId = productId;
        requestType = ProductsRequestType.FILTERED;
    }

    public ProductsRequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(ProductsRequestType requestType) {
        this.requestType = requestType;
    }

    public long[] getProductId() {
        return productId;
    }

    public void setProductId(long[] productId) {
        this.productId = productId;
    }
}
