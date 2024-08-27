package com.sks.gateway.recipes.rest;

import com.sks.recipes.api.RecipeRequestMessage;
import com.sks.recipes.api.RecipeResponseMessage;
import com.sks.recipes.api.RecipeSender;
import com.sks.recipes.api.dto.CreateRecipeDTO;
import com.sks.recipes.api.dto.RecipeDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Date;

@RestController
@RequestMapping("/recipes")
public class RecipesResource {
    private final RecipeSender sender;

    public RecipesResource(RecipeSender sender) {
        this.sender = sender;
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
    public RecipeDTO[] getMultipleRecipesById(@RequestBody int[] ids) {
        RecipeDTO[] recipes = new RecipeDTO[ids.length];
        for (int i = 0; i < ids.length; i++) {
            recipes[i] = new RecipeDTO(
                    ids[i],
                    "Flammkuchen",
                    "Flammkuchen ist ein dünner Fladenbrotteig, der mit Crème fraîche, Zwiebeln und Speck belegt wird...",
                    "/static/images/695f6e65b4bcd2cd19c7b0dd62b0fb82.png",
                    Date.from(Instant.now()),
                    "/users/42"
            );
        }
        return recipes;
    }

    @PostMapping
    @ResponseBody
    public RecipeDTO createRecipe(@RequestBody CreateRecipeDTO recipe) {
        final RecipeDTO mappedRecipe = map(recipe);
        mappedRecipe.setId(42);
        mappedRecipe.setCreationDate(Date.from(Instant.now()));
        return mappedRecipe;
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

    private RecipeDTO map(CreateRecipeDTO recipe) {
        return new RecipeDTO(
                0,
                recipe.getTitle(),
                recipe.getDescription(),
                recipe.getImgUri(),
                null,
                recipe.getOwnerUri()
        );
    }
}
