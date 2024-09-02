package com.sks.recipes.service.data.entity;

import com.sks.recipes.service.data.service.RecipeService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

// TODO
// THIS CLASS IS NOT USED IN THE FINAL IMPLEMENTATION
// IT IS ONLY USED FOR TESTING/DEMO PURPOSES
// WILL BE REMOVED BEFORE MERGING
@Component
public class AutoInsert {
    private final RecipeService recipeService;

    public AutoInsert(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    public void run() {
        if (!recipeService.getAll().isEmpty()) {
            return;
        }

        final RecipeEntity recipeEntity = new RecipeEntity();
        recipeEntity.setDescription("Auto inserted recipe");
        recipeEntity.setImageUri("https://upload.wikimedia.org/wikipedia/commons/e/ef/Pluto_in_True_Color_-_High-Res.jpg");
        recipeEntity.setPrivate(true);
        recipeEntity.setTitle("Hello World");
        recipeEntity.setOwnerUri("/users/id/1");
        recipeEntity.setLikedByUserUris(List.of("/users/id/1", "/users/id/2", "/users/id/3"));
        recipeEntity.setProductUris(List.of("/products/1", "/products/2", "/products/3"));
        recipeEntity.setProductQuantities(Map.of("/products/1", 1, "/products/2", 2, "/products/3", 3));

        recipeService.save(recipeEntity);

        final RecipeEntity recipeEntity2 = new RecipeEntity();
        recipeEntity2.setDescription("Auto inserted recipe");
        recipeEntity2.setImageUri("https://upload.wikimedia.org/wikipedia/commons/e/ef/Pluto_in_True_Color_-_High-Res.jpg");
        recipeEntity2.setPrivate(false);
        recipeEntity2.setTitle("MOINSEN");
        recipeEntity2.setOwnerUri("/users/id/2");
        recipeEntity2.setLikedByUserUris(List.of("/users/id/1", "/users/id/3"));
        recipeEntity2.setProductUris(List.of("/products/1", "/products/2", "/products/3", "/products/4"));
        recipeEntity2.setProductQuantities(Map.of("/products/1", 1, "/products/2", 2, "/products/3", 3, "/products/4", 4));

        recipeService.save(recipeEntity2);

        final List<RecipeEntity> ents = recipeService.getAll();
        for (RecipeEntity ent : ents) {
            System.out.println("Recipe: " + ent.getTitle());
            System.out.println("Private: " + ent.isPrivate());
            System.out.println("===============================");
        }
    }
}
