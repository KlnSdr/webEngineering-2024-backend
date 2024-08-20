package com.sks.recipes.service;

import com.sks.recipes.api.dto.RecipeDTO;
import com.sks.recipes.api.*;

import com.sks.recipes.service.data.entity.RecipeEntity;
import com.sks.recipes.service.data.service.RecipeService;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class Listener implements RecipesListener {
    private final RecipeSender sender;
    private final RecipeService service;

    public Listener(RecipeSender sender, RecipeService service) {
        this.sender = sender;
        this.service = service;
    }

    @Override
    public void listen(RecipeRequestMessage message) {
        final List<RecipeDTO> response = switch (message.getRequestType()) {
            case ByName -> getByName(message.getMessage());
            case ByPRODUCT -> getByProduct(message.getProducts());
        };
        sender.sendResponse(message, new RecipeResponseMessage(response));

    }

    private List<RecipeDTO> getByName(String searchString) {
        return service.findByName(searchString).stream().map(this::map).toList();
    }

    private List<RecipeDTO> getByProduct(String[] products) {
        return service.findByProducts(List.of(products)).stream().map(this::map).toList();
    }

    private RecipeDTO map(RecipeEntity recipeEntity) {
        return new RecipeDTO(recipeEntity.getId(), recipeEntity.getTitle(), recipeEntity.getDescription(), recipeEntity.getImageUri(), recipeEntity.isPrivate(), recipeEntity.getCreationDate(), recipeEntity.getOwnerUri(), recipeEntity.getLikedByUserUris(),recipeEntity.getProductUris(), recipeEntity.getProductQuantities());
    }
}
