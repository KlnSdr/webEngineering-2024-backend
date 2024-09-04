package com.sks.gateway.recipes.rest;

import com.sks.gateway.common.MessageErrorHandler;
import com.sks.gateway.util.UserHelper;
import com.sks.recipes.api.RecipeRequestMessage;
import com.sks.recipes.api.RecipeResponseMessage;
import com.sks.recipes.api.RecipeSender;
import com.sks.recipes.api.dto.CreateRecipeDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/recipes")
public class RecipesResource {
    private final UserHelper userHelper;
    private final RecipeSender sender;
    private final MessageErrorHandler messageErrorHandler;

    public RecipesResource(RecipeSender sender, UserHelper userHelper, MessageErrorHandler messageErrorHandler) {
        this.sender = sender;
        this.userHelper = userHelper;
        this.messageErrorHandler = messageErrorHandler;
    }

    @Operation(summary = "Get recipe by ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Found the recipe",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RecipeDTO.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Recipe not found",
                    content = @Content
            ),
            @ApiResponse(responseCode = "500", description = "Failed to send/receive message to/from service", content = @Content)
    })
    @GetMapping("/{id}")
    @ResponseBody
    public RecipeDTO getRecipeById(
            @Parameter(description = "ID of the recipe to be fetched") @PathVariable("id") int id) {
        final RecipeResponseMessage response = sender.sendRequest(RecipeRequestMessage.getById(id));

        if (response.didError()) {
            messageErrorHandler.handle(response);
        }

        if (response.getRecipes().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe with id " + id + " not found");
        }

        return response.getRecipes().getFirst();
    }

    @GetMapping("/user/{userId}")
    @ResponseBody
    public List<RecipeDTO> getRecipesByUser(@PathVariable("userId") long userId) {
        final RecipeResponseMessage response = sender.sendRequest(RecipeRequestMessage.getByOwnerId(userId));
        if (response.didError()) {
            messageErrorHandler.handle(response);
        }
        return response.getRecipes();
    }

    @Operation(summary = "Get multiple recipes by IDs")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Found the recipes",
                    content = {@Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RecipeDTO.class))
                    )}
            ),
            @ApiResponse(responseCode = "500", description = "Failed to send/receive message to/from service", content = @Content)
    })
    @PostMapping("/get-multiple")
    @ResponseBody
    public RecipeDTO[] getMultipleRecipesById(
            @Parameter(description = "IDs of the recipes to be fetched") @RequestBody long[] ids) {
        final RecipeResponseMessage response = sender.sendRequest(RecipeRequestMessage.getById(ids));
        if (response.didError()) {
            messageErrorHandler.handle(response);
        }
        return response.getRecipes().toArray(new RecipeDTO[0]);
    }

    @Operation(summary = "Create a new recipe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Recipe created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RecipeDTO.class)) }),
            @ApiResponse(responseCode = "401", description = "User not authenticated",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Failed to create recipe",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Failed to send/receive message to/from service", content = @Content)
    })
    @PostMapping
    @ResponseBody
    public RecipeDTO createRecipe(
            @Parameter(description = "Recipe to be created") @RequestBody CreateRecipeDTO recipe, Principal principal) {
        return createUpdateRecipe(recipe, principal);
    }

    @Operation(summary = "Update an existing recipe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RecipeDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Id in path and body do not match",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "User not authenticated",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Failed to update recipe",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Failed to send/receive message to/from service", content = @Content)
    })
    @PutMapping("/{id}")
    @ResponseBody
    public RecipeDTO updateRecipe(
            @Parameter(description = "ID of the recipe to be updated") @PathVariable("id") int id,
            @Parameter(description = "Updated recipe data") @RequestBody CreateRecipeDTO recipe, Principal principal) {
        if (id != recipe.getId()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id in path and body do not match");
        }
        return createUpdateRecipe(recipe, principal);
    }

    private RecipeDTO createUpdateRecipe(CreateRecipeDTO recipe, Principal principal) {
        final UserDTO user = userHelper.getCurrentInternalUser(principal);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }

        recipe.setOwnerUri("/users/id/" + user.getUserId());

        final RecipeResponseMessage response = sender.sendRequest(RecipeRequestMessage.update(recipe));

        if (response.didError()) {
            messageErrorHandler.handle(response);
        }

        if (response.getRecipe() == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create recipe");
        }

        return response.getRecipe();
    }

    @Operation(summary = "Delete a recipe by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Recipe deleted",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Failed to delete recipe",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Failed to send/receive message to/from service", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(
            @Parameter(description = "ID of the recipe to be deleted") @PathVariable("id") int id) {
        final RecipeResponseMessage response = sender.sendRequest(RecipeRequestMessage.delete(id));

        if (response.didError()) {
            messageErrorHandler.handle(response);
        }

        if (!response.isWasSuccessful()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete recipe with id " + id);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}