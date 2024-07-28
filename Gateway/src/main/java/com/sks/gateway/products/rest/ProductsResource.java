package com.sks.gateway.products.rest;

import com.sks.products.api.ProductDTO;
import com.sks.products.api.ProductsRequestMessage;
import com.sks.products.api.ProductsResponseMessage;
import com.sks.products.api.ProductsSender;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductsResource {
    private final ProductsSender productsSender;

    public ProductsResource(ProductsSender productsSender) {
        this.productsSender = productsSender;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ProductDTO getProductById(@PathVariable("id") int id) {
        final ProductsResponseMessage response = productsSender.sendRequest(new ProductsRequestMessage(id));
        final ProductDTO product = response.getProduct();

        return product;
    }

    @PostMapping(value = "get-multiple", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ProductResponse[] getMultiple(@RequestBody int[] ids) {
        ProductResponse[] responses = new ProductResponse[ids.length];
        for (int i = 0; i < ids.length; i++) {
            responses[i] = new ProductResponse(ids[i], "Streuselkäse", "t");
        }
        return responses;
    }
}
