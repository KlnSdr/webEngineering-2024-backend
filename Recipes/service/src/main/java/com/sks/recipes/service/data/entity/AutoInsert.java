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
        final RecipeEntity recipeEntity = new RecipeEntity();
        recipeEntity.setDescription("Auto inserted recipe");
        recipeEntity.setImageUri("https://example.com/image.jpg");
        recipeEntity.setPrivate(true);
        recipeEntity.setTitle("Hello World");
        recipeEntity.setOwnerUri("https://example.com/owner");
        recipeEntity.setLikedByUserUris(List.of("https://example.com/user", "https://example.com/user2", "https://example.com/user3"));
        recipeEntity.setProductUris(List.of("https://example.com/product", "https://example.com/product2", "https://example.com/product3"));
        recipeEntity.setProductQuantities(Map.of("https://example.com/product", 1, "https://example.com/product2", 2, "https://example.com/product3", 3));

        recipeService.save(recipeEntity);

        final RecipeEntity recipeEntity2 = new RecipeEntity();
        recipeEntity2.setDescription("Auto inserted recipe");
        recipeEntity2.setImageUri("https://example.com/image.PNG");
        recipeEntity2.setPrivate(false);
        recipeEntity2.setTitle("MOINSEN");
        recipeEntity2.setOwnerUri("https://example.com/owner2");
        recipeEntity2.setLikedByUserUris(List.of("https://example.com/user", "https://example.com/user3"));
        recipeEntity2.setProductUris(List.of("https://example.com/product", "https://example.com/product2", "https://example.com/product3", "https://example.com/product4"));
        recipeEntity2.setProductQuantities(Map.of("https://example.com/product", 1, "https://example.com/product2", 2, "https://example.com/product3", 3, "https://example.com/product4", 4));

        recipeService.save(recipeEntity2);

        final List<RecipeEntity> ents = recipeService.getAll();
        for (RecipeEntity ent : ents) {
            System.out.println("Recipe: " + ent.getTitle());
            System.out.println("Private: " + ent.isPrivate());
            System.out.println("===============================");
        }
    }
}
