package com.sks.gateway.products.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductsResource {

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ProductResponse getProductById(@PathVariable("id") int id) {
        return new ProductResponse(id, "Streuselkäse", "t");
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
