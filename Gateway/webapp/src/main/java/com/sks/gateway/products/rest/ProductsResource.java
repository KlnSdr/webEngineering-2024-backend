package com.sks.gateway.products.rest;

import com.sks.gateway.common.MessageErrorHandler;
import com.sks.products.api.ProductDTO;
import com.sks.products.api.ProductsRequestMessage;
import com.sks.products.api.ProductsResponseMessage;
import com.sks.products.api.ProductsSender;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/products")
public class ProductsResource {
    private final ProductsSender productsSender;
    private final MessageErrorHandler messageErrorHandler;

    public ProductsResource(ProductsSender productsSender, MessageErrorHandler messageErrorHandler) {
        this.productsSender = productsSender;
        this.messageErrorHandler = messageErrorHandler;
    }

    @Operation(summary = "Get all products")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Found all products",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ProductDTO.class))
                            )
                    })
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ProductDTO[] getAllProducts() {
        final ProductsResponseMessage response = productsSender.sendRequest(new ProductsRequestMessage());

        if (response.didError()) {
            messageErrorHandler.handle(response);
        }

        return response.getProducts();
    }

    @Operation(summary = "Get product by ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Found the product",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProductDTO.class)
                            )
                    }),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ProductDTO getProductById(
            @Parameter(description = "ID of the product to be fetched") @PathVariable("id") long id) {
        final ProductsResponseMessage response = productsSender.sendRequest(new ProductsRequestMessage(new long[] {id}));

        if (response.didError()) {
            messageErrorHandler.handle(response);
        }

        final ProductDTO[] product = response.getProducts();

        if (product == null || product.length == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product with id " + id + " not found");
        }

        return product[0];
    }

    @Operation(summary = "Get multiple products by IDs")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Found the products",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ProductDTO.class))
                            )
                    }),
            @ApiResponse(responseCode = "404", description = "Products not found", content = @Content)
    })
    @PostMapping(value = "get-multiple", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ProductDTO[] getMultiple(
            @Parameter(description = "IDs of the products to be fetched") @RequestBody long[] ids) {
        final ProductsResponseMessage response = productsSender.sendRequest(new ProductsRequestMessage(ids));

        if (response.didError()) {
            messageErrorHandler.handle(response);
        }

        final ProductDTO[] products = response.getProducts();

        if (products == null || products.length != ids.length) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Products not found");
        }

        return products;
    }
}