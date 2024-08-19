package com.sks.gateway.recipes.rest;

import com.sks.gateway.recipes.dto.RecipeDTO;
import com.sks.recipes.api.RecipeRequestMessage;
import com.sks.recipes.api.RecipeResponseMessage;
import com.sks.recipes.api.RecipeSender;
import jakarta.websocket.server.PathParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/search/recipes")
public class SearchResource {
    private final RecipeSender recipeSender;

    public SearchResource(RecipeSender recipeSender) {
        this.recipeSender = recipeSender;
    }
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<RecipeDTO> getAllRecipesBySearchString(@PathParam("searchString") String searchString) {
        final RecipeResponseMessage response = recipeSender.sendRequest(new RecipeRequestMessage(searchString));
        final List<RecipeDTO> recipes = response.getRecipes();

        if (recipes == null || recipes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipes with search string " + searchString + " not found");
        }

        return recipes;
    }



    @PostMapping (value = "/by-products", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<RecipeDTO> searchRecipeByProducts(@RequestBody String[] products) {
        final RecipeResponseMessage response = recipeSender.sendRequest(new RecipeRequestMessage(products));
        final List<RecipeDTO> recipes = response.getRecipes();

        if (recipes == null || recipes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipes with products" + Arrays.toString(products) + " not found");
        }

        return recipes;
    }
}