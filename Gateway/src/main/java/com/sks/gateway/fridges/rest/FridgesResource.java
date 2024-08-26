package com.sks.gateway.fridges.rest;

import com.sks.fridge.api.*;
import com.sks.gateway.fridges.dto.FridgeItemDTO;
import com.sks.gateway.util.AccessVerifier;
import com.sks.products.api.ProductDTO;
import com.sks.products.api.ProductsRequestMessage;
import com.sks.products.api.ProductsResponseMessage;
import com.sks.products.api.ProductsSender;
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

    //Integrating the ProductsSender for communicating with the product service
    private final ProductsSender productsSender;
    private final FridgeSender fridgeSender;
    private final AccessVerifier accessVerifier;

    public FridgesResource(ProductsSender productsSender, FridgeSender fridgeSender, AccessVerifier accessVerifier) {
        this.productsSender = productsSender;
        this.fridgeSender = fridgeSender;
        this.accessVerifier = accessVerifier;
    }

    //Get information of fridge items
    @GetMapping("/{userId}")
    @ResponseBody
    public List<FridgeItemDTO> getFridgeItems(@PathVariable("userId") long userId, Principal principal) {
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

    //Add a new product to fridge or update an existing
    @PutMapping("/{userId}")
    @ResponseBody
    public List<FridgeItemDTO> addOrUpdateFridgeItems(
            @PathVariable("userId") long userId,
            @RequestBody List<FridgeAddItemDTO> items, Principal principal) {
        if (!accessVerifier.verifyAccessesSelf(userId, principal)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        final FridgeResponseMessage response = fridgeSender.sendRequest(FridgeRequestMessage.updateByUserId(userId, items));

        if (!response.isWasSuccess()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, response.getMessage());
        }

        final FridgeDTO fridge = response.getFridgeContent();
        return map(fridge);
    }

    //Delete product from fridge
    @DeleteMapping("/{userId}/{productId}")
    public ResponseEntity<Void> deleteFridgeItem(
            @PathVariable("userId") long userId,
            @PathVariable("productId") long productId, Principal principal) {
        if (!accessVerifier.verifyAccessesSelf(userId, principal)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        final FridgeResponseMessage response = fridgeSender.sendRequest(FridgeRequestMessage.deleteByUserAndProduct(userId, productId));
        if (!response.isWasSuccess()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, response.getMessage());
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
