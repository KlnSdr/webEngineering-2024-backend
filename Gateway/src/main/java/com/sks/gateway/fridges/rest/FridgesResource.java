package com.sks.gateway.fridges.rest;

import com.sks.gateway.fridges.dto.FridgeAddItemDTO;
import com.sks.gateway.fridges.dto.FridgeItemDTO;
import com.sks.products.api.ProductDTO;
import com.sks.products.api.ProductsRequestMessage;
import com.sks.products.api.ProductsResponseMessage;
import com.sks.products.api.ProductsSender;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("/fridges")
public class FridgesResource {

    //Integrating the ProductsSender for communicating with the product service
    private final ProductsSender productsSender;

    public FridgesResource(ProductsSender productsSender) {
        this.productsSender = productsSender;
    }

    //Get information of fridge items
    @GetMapping("/{userId}")
    @ResponseBody
    public List<FridgeItemDTO> getFridgeItems(@PathVariable("userId") long userId) {

        return List.of(new FridgeItemDTO("Milk", 1, "liters", 2.5), new FridgeItemDTO("Flour", 2, "kg", 3));

    }

    //Delete product from fridge
    @DeleteMapping("/{userId}/{productId}")
    public ResponseEntity<Void> deleteFridgeItem(
            @PathVariable("userId") long userId,
            @PathVariable("productId") long productId) {

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
