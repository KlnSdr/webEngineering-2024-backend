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
        return List.of( new RecipeDTO(1, "Käsesoße", "https://via.placeholder.com/150", "Soße aus Käse", Date.from(Instant.now()), "/users/42"),
                new RecipeDTO(2, "Tomtensoße", "https://via.placeholder.com/150", "Soße aus Tomaten", Date.from(Instant.now()), "/users/42"),
                new RecipeDTO(3, "Käsebrot", "https://via.placeholder.com/150", "Brot mit Käse", Date.from(Instant.now()), "/users/42"));
    }
}