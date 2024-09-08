package com.sks.gateway.recipes.rest;

import com.sks.gateway.common.MessageErrorHandler;
import com.sks.gateway.util.UserHelper;
import com.sks.recipes.api.RecipeRequestMessage;
import com.sks.recipes.api.RecipeResponseMessage;
import com.sks.recipes.api.RecipeSender;
import com.sks.recipes.api.dto.RecipeDTO;
import com.sks.users.api.UserDTO;
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

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/search/recipes")
public class SearchResource {
    private final RecipeSender recipeSender;
    private final MessageErrorHandler messageErrorHandler;
    private final UserHelper userHelper;

    public SearchResource(RecipeSender recipeSender, MessageErrorHandler messageErrorHandler, UserHelper userHelper) {
        this.recipeSender = recipeSender;
        this.messageErrorHandler = messageErrorHandler;
        this.userHelper = userHelper;
    }

    @Operation(summary = "Get all recipes by search string")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the recipes",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RecipeDTO.class)))),
            @ApiResponse(responseCode = "404", description = "Recipes not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Failed to send/receive message to/from service", content = @Content)
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<RecipeDTO> getAllRecipesBySearchString(
            @Parameter(description = "Search string to find recipes") @RequestParam("searchString") String searchString, Principal principal) {
        final RecipeResponseMessage response = recipeSender.sendRequest(new RecipeRequestMessage(searchString));

        if (response.didError()) {
            messageErrorHandler.handle(response);
        }

        final List<RecipeDTO> recipes = response.getRecipes();

        if (recipes == null || recipes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipes with search string " + searchString + " not found");
        }

        final UserDTO currentUser = userHelper.getCurrentInternalUser(principal);

        return filterForPrivateRecipes(recipes, currentUser);
    }

    @Operation(summary = "Search recipes by products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the recipes",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RecipeDTO.class)))),
            @ApiResponse(responseCode = "404", description = "Recipes not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Failed to send/receive message to/from service", content = @Content)
    })
    @PostMapping(value = "/by-products", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<RecipeDTO> searchRecipeByProducts(
            @Parameter(description = "List of products to search recipes by") @RequestBody String[] products, Principal principal) {
        final RecipeResponseMessage response = recipeSender.sendRequest(new RecipeRequestMessage(products));

        if (response.didError()) {
            messageErrorHandler.handle(response);
        }

        final List<RecipeDTO> recipes = response.getRecipes();

        if (recipes == null || recipes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipes with products" + Arrays.toString(products) + " not found");
        }

        final UserDTO currentUser = userHelper.getCurrentInternalUser(principal);

        return filterForPrivateRecipes(recipes, currentUser);
    }

    private List<RecipeDTO> filterForPrivateRecipes(List<RecipeDTO> recipes, UserDTO user) {
        final String currentUserUri = user == null ? "" : "/users/id/" + user.getUserId();
        return recipes.stream()
                .filter(recipe -> !recipe.isPrivate() || recipe.getOwnerUri().equals(currentUserUri))
                .collect(Collectors.toList());
    }
}