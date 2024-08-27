package com.sks.recipes.service;

import com.sks.recipes.api.dto.CreateRecipeDTO;
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
        final RecipeResponseMessage response = switch (message.getRequestType()) {
            case GET_BY_ID -> getById(message.getIds());
            case UPDATE -> update(message.getRecipe());
            case DELETE -> delete(message.getIds());
            case SEARCH_BY_NAME -> getByName(message.getMessage());
            case SEARCH_BY_PRODUCTS -> getByProduct(message.getProducts());
        };
        sender.sendResponse(message, response);
    }

    private RecipeResponseMessage getById(long[] id) {
        return null;
    }

    private RecipeResponseMessage update(CreateRecipeDTO recipe) {
        return null;
    }

    private RecipeResponseMessage delete(long[] ids) {
        return null;
    }

    private RecipeResponseMessage getByName(String searchString) {
        final List<RecipeDTO> recipes = service.findByName(searchString).stream().map(this::map).toList();

        final RecipeResponseMessage response = new RecipeResponseMessage();
        response.setRecipes(recipes);
        return response;
    }

    private RecipeResponseMessage getByProduct(String[] products) {
        final List<RecipeDTO> recipes = service.findByProducts(List.of(products)).stream().map(this::map).toList();

        final RecipeResponseMessage response = new RecipeResponseMessage();
        response.setRecipes(recipes);
        return response;
    }

    private RecipeDTO map(RecipeEntity recipeEntity) {
        return new RecipeDTO(recipeEntity.getId(), recipeEntity.getTitle(), recipeEntity.getDescription(), recipeEntity.getImageUri(), recipeEntity.isPrivate(), recipeEntity.getCreationDate(), recipeEntity.getOwnerUri(), recipeEntity.getLikedByUserUris(),recipeEntity.getProductUris(), recipeEntity.getProductQuantities());
    }
}
