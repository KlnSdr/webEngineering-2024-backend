package com.sks.products.api;

import com.sks.base.api.BaseSender;

public interface ProductsSender extends BaseSender<ProductsRequestMessage, ProductsResponseMessage> {
    ProductsResponseMessage sendRequest(ProductsRequestMessage message);
    void sendResponse(ProductsRequestMessage request, ProductsResponseMessage response);
}
