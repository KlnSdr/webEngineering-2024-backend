package com.sks.gateway.recipes.rest;

import com.sks.gateway.recipes.dto.RecipeDTO;
import jakarta.websocket.server.PathParam;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/search/recipes")
public class SearchResource {

    @GetMapping
    @ResponseBody
    public List<RecipeDTO> searchRecipes(@PathParam("searchTerm") String searchTerm) {
        return List.of(new RecipeDTO(1, searchTerm, "https://via.placeholder.com/150", "Soße aus Käse", Date.from(Instant.now()), "/users/42"));
    }

    @PostMapping ("/by-products")
    @ResponseBody
    public List<RecipeDTO> searchRecipeByProducts(@RequestBody String[] products) {
        RecipeDTO[] recipes = new RecipeDTO[3];
        recipes[0] = new RecipeDTO(1, "Käsesoße", "https://via.placeholder.com/150", "Soße aus Käse", Date.from(Instant.now()), "/users/42");
        recipes[1] = new RecipeDTO(2, "Tomatensoße", "https://via.placeholder.com/150", "Soße aus Tomaten", Date.from(Instant.now()), "/users/42");
        recipes[2] = new RecipeDTO(3, "Leckere Käseoße", "https://via.placeholder.com/150", "Soße aus gutem Käse und leckeren Milch", Date.from(Instant.now()), "/users/42");

        List<RecipeDTO> selectedRecipe = List.of();
        for (int i = 0; i < recipes.length; i++) {
         selectedRecipe.add(recipes[i]);
        }
        return selectedRecipe;
    }
}