package com.sks.gateway.products.rest;

import com.sks.products.api.ProductDTO;
import com.sks.products.api.ProductsRequestMessage;
import com.sks.products.api.ProductsResponseMessage;
import com.sks.products.api.ProductsSender;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

        if (product == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product with id " + id + " not found");
        }

        return product;
    }

    @PostMapping(value = "get-multiple", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ProductDTO[] getMultiple(@RequestBody long[] ids) {
        final ProductDTO[] products = new ProductDTO[ids.length];

        for (int i = 0; i < ids.length; i++) {
            final ProductsResponseMessage response = productsSender.sendRequest(new ProductsRequestMessage(ids[i]));
            products[i] = response.getProduct();

            if (products[i] == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product with id + "+ids[i] + " not found");
            }
        }

        return products;
    }
}
