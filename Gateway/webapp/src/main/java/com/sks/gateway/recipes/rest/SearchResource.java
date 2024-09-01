package com.sks.gateway.recipes.rest;

import com.sks.recipes.api.RecipeRequestMessage;
import com.sks.recipes.api.RecipeResponseMessage;
import com.sks.recipes.api.RecipeSender;
import com.sks.recipes.api.dto.RecipeDTO;
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

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/search/recipes")
public class SearchResource {
    private final RecipeSender recipeSender;

    public SearchResource(RecipeSender recipeSender) {
        this.recipeSender = recipeSender;
    }

    @Operation(summary = "Get all recipes by search string")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the recipes",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RecipeDTO.class)))),
            @ApiResponse(responseCode = "404", description = "Recipes not found",
                    content = @Content)
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<RecipeDTO> getAllRecipesBySearchString(
            @Parameter(description = "Search string to find recipes") @RequestParam("searchString") String searchString) {
        final RecipeResponseMessage response = recipeSender.sendRequest(new RecipeRequestMessage(searchString));
        final List<RecipeDTO> recipes = response.getRecipes();

        if (recipes == null || recipes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipes with search string " + searchString + " not found");
        }

        return recipes;
    }

    @Operation(summary = "Search recipes by products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the recipes",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RecipeDTO.class)))),
            @ApiResponse(responseCode = "404", description = "Recipes not found",
                    content = @Content)
    })
    @PostMapping(value = "/by-products", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<RecipeDTO> searchRecipeByProducts(
            @Parameter(description = "List of products to search recipes by") @RequestBody String[] products) {
        final RecipeResponseMessage response = recipeSender.sendRequest(new RecipeRequestMessage(products));
        final List<RecipeDTO> recipes = response.getRecipes();

        if (recipes == null || recipes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipes with products" + Arrays.toString(products) + " not found");
        }

        return recipes;
    }
}