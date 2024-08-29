package com.sks.gateway.fridges.rest;

import com.sks.fridge.api.*;
import com.sks.gateway.fridges.dto.FridgeItemDTO;
import com.sks.gateway.util.AccessVerifier;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/fridge")
public class FridgesResource {

    private final ProductsSender productsSender;
    private final FridgeSender fridgeSender;
    private final AccessVerifier accessVerifier;

    public FridgesResource(ProductsSender productsSender, FridgeSender fridgeSender, AccessVerifier accessVerifier) {
        this.productsSender = productsSender;
        this.fridgeSender = fridgeSender;
        this.accessVerifier = accessVerifier;
    }

    @Operation(summary = "Get information of fridge items")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Found the fridge items",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = FridgeItemDTO.class))
                            )
                    }),
            @ApiResponse(responseCode = "403", description = "Access denied",content = @Content),
            @ApiResponse(responseCode = "404", description = "Fridge not found", content = @Content)
    })
    @GetMapping("/{userId}")
    @ResponseBody
    public List<FridgeItemDTO> getFridgeItems(
            @Parameter(description = "ID of the user whose fridge items are to be fetched") @PathVariable("userId") long userId,
            Principal principal) {
        if (!accessVerifier.verifyAccessesSelf(userId, principal)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        final FridgeResponseMessage response = fridgeSender.sendRequest(FridgeRequestMessage.getByUserId(userId));
        final FridgeDTO fridge = response.getFridgeContent();
        if (fridge == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Fridge not found");
        }
        return map(fridge);
    }

    @Operation(summary = "Add a new product to fridge or update an existing one")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Fridge items updated",
                    content = {
                    @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = FridgeItemDTO.class))
                    )
            }),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PutMapping("/{userId}")
    @ResponseBody
    public List<FridgeItemDTO> addOrUpdateFridgeItems(
            @Parameter(description = "ID of the user whose fridge items are to be updated") @PathVariable("userId") long userId,
            @RequestBody List<FridgeAddItemDTO> items, Principal principal) {
        if (!accessVerifier.verifyAccessesSelf(userId, principal)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        final FridgeResponseMessage response = fridgeSender.sendRequest(FridgeRequestMessage.updateByUserId(userId, items));

        if (!response.isWasSuccess()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, response.getErrorMessage());
        }

        final FridgeDTO fridge = response.getFridgeContent();
        return map(fridge);
    }

    @Operation(summary = "Delete product from fridge")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content)
    })
    @DeleteMapping("/{userId}/{productId}")
    public ResponseEntity<Void> deleteFridgeItem(
            @Parameter(description = "ID of the user whose fridge item is to be deleted") @PathVariable("userId") long userId,
            @Parameter(description = "ID of the product to be deleted") @PathVariable("productId") long productId, Principal principal) {
        if (!accessVerifier.verifyAccessesSelf(userId, principal)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        final FridgeResponseMessage response = fridgeSender.sendRequest(FridgeRequestMessage.deleteByUserAndProduct(userId, productId));
        if (!response.isWasSuccess()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, response.getErrorMessage());
        }
        return ResponseEntity.noContent().build();
    }

    private ProductDTO[] getProductsByIds(long[] ids) {
        final ProductsResponseMessage response = productsSender.sendRequest(new ProductsRequestMessage(ids));
        return response.getProducts();
    }

    private List<FridgeItemDTO> map(FridgeDTO fridge) {
        // ["/products/42"] -> ["42"] -> List(42L) -> (long[])[42]
        final long[] productIds = fridge.getProducts().keySet().stream().map(i -> i.replace("/products/", "")).map(Long::parseLong).toList().stream().mapToLong(Long::longValue).toArray();
        final ProductDTO[] products = getProductsByIds(productIds);

        return Arrays.stream(products).map(product -> combine(product, fridge.getProducts().get("/products/" + product.getId()))).toList();
    }

    private FridgeItemDTO combine(ProductDTO product, Integer quantity) {
        return new FridgeItemDTO(product.getName(), product.getId(), product.getUnit(), quantity);
    }
}