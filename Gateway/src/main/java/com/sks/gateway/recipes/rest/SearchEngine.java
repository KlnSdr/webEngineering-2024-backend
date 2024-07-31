package com.sks.gateway.recipes.rest;

import com.sks.gateway.recipes.dto.RecipeDTO;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/search/recipe")
public class SearchEngine {

    @GetMapping
    @ResponseBody
    public List<RecipeDTO> searchRecipes(@RequestParam("searchTerm") String searchTerm) {
        RecipeDTO[] recipes = new RecipeDTO[3];
        recipes[0] = new RecipeDTO(1, "Käsesoße", "https://via.placeholder.com/150", "Soße aus Käse", Date.from(Instant.now()), "/users/42");
        recipes[1] = new RecipeDTO(2, "Tomatensoße", "https://via.placeholder.com/150", "Soße aus Tomaten", Date.from(Instant.now()), "/users/42");
        recipes[2] = new RecipeDTO(3, "Leckere Käseoße", "https://via.placeholder.com/150", "Soße aus gutem Käse", Date.from(Instant.now()), "/users/42");

        List<RecipeDTO> selectedRecipe = new ArrayList<>();
        for (RecipeDTO recipe : recipes) {
            if (recipe.getTitle().contains(searchTerm)) {
                selectedRecipe.add(recipe);
            }
        }
        return selectedRecipe;
    }

    @PostMapping ("/by-products")
    @ResponseBody
    public List<RecipeDTO> searchRecipeByProducts(@RequestBody String[] products) {
        RecipeDTO[] recipes = new RecipeDTO[3];
        recipes[0] = new RecipeDTO(1, "Käsesoße", "https://via.placeholder.com/150", "Soße aus Käse", Date.from(Instant.now()), "/users/42");
        recipes[1] = new RecipeDTO(2, "Tomatensoße", "https://via.placeholder.com/150", "Soße aus Tomaten", Date.from(Instant.now()), "/users/42");
        recipes[2] = new RecipeDTO(3, "Leckere Käseoße", "https://via.placeholder.com/150", "Soße aus gutem Käse und leckeren Milch", Date.from(Instant.now()), "/users/42");

        List<RecipeDTO> selectedRecipe = new ArrayList<>();
        for (int i = 0; i < recipes.length; i++) {
            boolean containsAll = true;
            for (int j = 0; j < products.length; j++) {
                if (!recipes[i].getDescription().contains(products[j])) {
                    containsAll = false;
                    break;
                }
            }
            if (containsAll) {
                selectedRecipe.add(recipes[i]);
            }
        }

        return selectedRecipe;
    }
}