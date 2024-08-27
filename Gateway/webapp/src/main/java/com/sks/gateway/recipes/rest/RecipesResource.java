package com.sks.gateway.recipes.rest;

import com.sks.gateway.util.UserHelper;
import com.sks.recipes.api.RecipeRequestMessage;
import com.sks.recipes.api.RecipeResponseMessage;
import com.sks.recipes.api.RecipeSender;
import com.sks.recipes.api.dto.CreateRecipeDTO;
import com.sks.recipes.api.dto.RecipeDTO;
import com.sks.users.api.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RestController
@RequestMapping("/recipes")
public class RecipesResource {
    private final UserHelper userHelper;
    private final RecipeSender sender;

    public RecipesResource(RecipeSender sender, UserHelper userHelper) {
        this.sender = sender;
        this.userHelper = userHelper;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public RecipeDTO getRecipeById(@PathVariable("id") int id) {
        final RecipeResponseMessage response = sender.sendRequest(RecipeRequestMessage.getById(id));

        if (response.getRecipes().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe with id " + id + " not found");
        }

        return response.getRecipes().getFirst();
    }

    @PostMapping("/get-multiple")
    @ResponseBody
    public RecipeDTO[] getMultipleRecipesById(@RequestBody long[] ids) {
        final RecipeResponseMessage response = sender.sendRequest(RecipeRequestMessage.getById(ids));
        return response.getRecipes().toArray(new RecipeDTO[0]);
    }

    @PostMapping
    @ResponseBody
    public RecipeDTO createRecipe(@RequestBody CreateRecipeDTO recipe, Principal principal) {
        final UserDTO user = userHelper.getCurrentInternalUser(principal);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }

        recipe.setOwnerUri("/users/" + user.getUserId());

        final RecipeResponseMessage response = sender.sendRequest(RecipeRequestMessage.update(recipe));

        if (response.getRecipe() == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create recipe");
        }

        return response.getRecipe();
    }

    @PutMapping("/{id}")
    @ResponseBody
    public RecipeDTO updateRecipe(@PathVariable("id") int id, @RequestBody RecipeDTO recipe) {
        return recipe;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable("id") int id) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
