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

    //Get information of fridge items
    @GetMapping("/{userId}")
    @ResponseBody
    public List<FridgeItemDTO> getFridgeItems(@PathVariable("userId") long userId) {

        return List.of(new FridgeItemDTO("Milk", 1, "liters", 2.5), new FridgeItemDTO("Flour", 2, "kg", 3));

    }

}
