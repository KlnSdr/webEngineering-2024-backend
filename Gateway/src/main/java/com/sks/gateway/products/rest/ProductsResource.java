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

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ProductDTO[] getAllProducts() {
        final ProductsResponseMessage response = productsSender.sendRequest(new ProductsRequestMessage());
        return response.getProducts();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ProductDTO getProductById(@PathVariable("id") long id) {
        final ProductsResponseMessage response = productsSender.sendRequest(new ProductsRequestMessage(new long[] {id}));
        final ProductDTO[] product = response.getProducts();

        if (product == null || product.length == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product with id " + id + " not found");
        }

        return product[0];
    }

    @PostMapping(value = "get-multiple", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ProductDTO[] getMultiple(@RequestBody long[] ids) {
        final ProductsResponseMessage response = productsSender.sendRequest(new ProductsRequestMessage(ids));
        final ProductDTO[] products = response.getProducts();

        if (products == null || products.length != ids.length) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Products not found");
        }

        return products;
    }
}
