package com.sks.gateway;

import com.sks.products.api.ProductsRequestMessage;
import com.sks.products.api.ProductsResponseMessage;
import com.sks.products.api.ProductsSender;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductsResource {
    private final ProductsSender productsSender;

    public ProductsResource(ProductsSender productsSender) {
        this.productsSender = productsSender;
    }

    @GetMapping(value = "/send", produces = MediaType.APPLICATION_JSON_VALUE)
    public String send() {
        final ProductsResponseMessage response = productsSender.sendRequest(new ProductsRequestMessage("Hello from gateway"));
        return response.getMessage();
    }
}
