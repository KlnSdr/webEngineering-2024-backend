package com.sks.gateway.recipes.rest;

import com.sks.recipes.api.dto.CreateRecipeDTO;
import com.sks.recipes.api.dto.RecipeDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;

@RestController
@RequestMapping("/recipes")
public class RecipesResource {

    @GetMapping("/{id}")
    @ResponseBody
    public RecipeDTO getRecipeById(@PathVariable("id") int id) {
        return new RecipeDTO(
                id,
                "Flammkuchen",
                "/static/images/695f6e65b4bcd2cd19c7b0dd62b0fb82.png",
                "Flammkuchen ist ein dünner Fladenbrotteig, der mit Crème fraîche, Zwiebeln und Speck belegt wird...",
                Date.from(Instant.now()),
                "/users/42"
        );
    }

    @PostMapping("/get-multiple")
    @ResponseBody
    public RecipeDTO[] getMultipleRecipesById(@RequestBody int[] ids) {
        RecipeDTO[] recipes = new RecipeDTO[ids.length];
        for (int i = 0; i < ids.length; i++) {
            recipes[i] = new RecipeDTO(
                    ids[i],
                    "Flammkuchen",
                    "/static/images/695f6e65b4bcd2cd19c7b0dd62b0fb82.png",
                    "Flammkuchen ist ein dünner Fladenbrotteig, der mit Crème fraîche, Zwiebeln und Speck belegt wird...",
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
                recipe.getImgUri(),
                recipe.getDescription(),
                null,
                recipe.getOwnerUri()
        );
    }
}
