package com.sks.recipes.service;

import com.sks.recipes.api.dto.CreateRecipeDTO;
import com.sks.recipes.api.RecipeRequestMessage;
import com.sks.recipes.api.RecipeResponseMessage;
import com.sks.recipes.api.RecipeSender;
import com.sks.recipes.api.RecipesListener;
import com.sks.recipes.api.dto.RecipeDTO;
import com.sks.recipes.service.data.entity.RecipeEntity;
import com.sks.recipes.service.data.service.RecipeService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


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
        RecipeResponseMessage response;
        try {
            response = switch (message.getRequestType()) {
                case GET_BY_ID -> getById(message.getIds());
                case GET_BY_OWNER_ID -> getByOwnerId(message.getOwnerId());
                case UPDATE -> update(message.getRecipe());
                case DELETE -> delete(message.getIds());
                case SEARCH_BY_NAME -> getByName(message.getMessage());
                case SEARCH_BY_PRODUCTS -> getByProduct(message.getProducts());
            };
        } catch (Exception e) {
            response = buildErrorResponse(e);
        }
        sender.sendResponse(message, response);
    }

    private RecipeResponseMessage buildErrorResponse(Exception e) {
        RecipeResponseMessage response = new RecipeResponseMessage();
        response.setDidError(true);
        response.setErrorMessage("Error while processing message");
        response.setException(e);
        return response;
    }

    private RecipeResponseMessage getByOwnerId(long ownerId) {
        final List<RecipeDTO> recipes = service.findByOwner("/users/id/" + ownerId).stream().map(this::map).toList();

        final RecipeResponseMessage response = new RecipeResponseMessage();
        response.setRecipes(recipes);
        return response;
    }

    private RecipeResponseMessage getById(long[] ids) {
        final List<RecipeDTO> recipes = new ArrayList<>();

        for (long id : ids) {
            final Optional<RecipeEntity> recipe = service.findById(id);
            recipe.ifPresent(entity -> recipes.add(map(entity)));
        }

        final RecipeResponseMessage response = new RecipeResponseMessage();
        response.setRecipes(recipes);
        return response;
    }

    private RecipeResponseMessage update(CreateRecipeDTO recipe) {
        final RecipeEntity entity = map(recipe);
        final RecipeEntity savedEntity = service.save(entity);

        final RecipeResponseMessage response = new RecipeResponseMessage();
        response.setRecipe(map(savedEntity));
        return response;
    }

    private RecipeResponseMessage delete(long[] ids) {
        boolean success = true;
        List<Long> notDeletedIds = new ArrayList<>();

        for (long id : ids) {
            if (!service.deleteById(id)) {
                success = false;
                notDeletedIds.add(id);
            }
        }

        final RecipeResponseMessage response = new RecipeResponseMessage();
        response.setWasSuccessful(success);
        response.setNotDeletedIds(map(notDeletedIds));
        return response;
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
        return new RecipeDTO(recipeEntity.getId(), recipeEntity.getTitle(), recipeEntity.getDescription(), recipeEntity.getImageUri(), recipeEntity.isPrivate(), recipeEntity.getCreationDate(), recipeEntity.getOwnerUri(), recipeEntity.getLikedByUserUris(), recipeEntity.getProductUris(), recipeEntity.getProductQuantities());
    }

    private long[] map(List<Long> ids) {
        return ids.stream().mapToLong(Long::longValue).toArray();
    }

    private RecipeEntity map(CreateRecipeDTO recipe) {
        final RecipeEntity entity = new RecipeEntity();
        entity.setTitle(recipe.getTitle());
        entity.setDescription(recipe.getDescription());
        entity.setImageUri(recipe.getImgUri());
        entity.setOwnerUri(recipe.getOwnerUri());
        entity.setPrivate(recipe.isPrivate());
        entity.setProductQuantities(recipe.getProductQuantities());

        final List<String> productUris = new ArrayList<>();
        recipe.getProductQuantities().forEach((uri, amount) -> productUris.add(uri));
        entity.setProductUris(productUris);

        entity.setLikedByUserUris(new ArrayList<>());

        final long id = recipe.getId();
        if (id > 0) {
            entity.setId(id);
        }

        return entity;
    }
}
