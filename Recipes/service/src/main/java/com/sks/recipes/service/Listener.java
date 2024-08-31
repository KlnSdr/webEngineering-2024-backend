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

/**
 * Listener class for handling recipe-related messages.
 */
@Component
public class Listener implements RecipesListener {
    private final RecipeSender sender;
    private final RecipeService service;

    /**
     * Constructs a new Listener with the specified RecipeSender and RecipeService.
     *
     * @param sender the sender for sending recipe responses
     * @param service the service for managing recipes
     */
    public Listener(RecipeSender sender, RecipeService service) {
        this.sender = sender;
        this.service = service;
    }

    /**
     * Listens for recipe request messages and processes them.
     *
     * @param message the recipe request message
     */
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

    /**
     * Retrieves recipes by their IDs.
     *
     * @param ids the IDs of the recipes to retrieve
     * @return the response message containing the retrieved recipes
     */
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

    /**
     * Updates a recipe.
     *
     * @param recipe the recipe to update
     * @return the response message containing the updated recipe
     */
    private RecipeResponseMessage update(CreateRecipeDTO recipe) {
        final RecipeEntity entity = map(recipe);
        final RecipeEntity savedEntity = service.save(entity);

        final RecipeResponseMessage response = new RecipeResponseMessage();
        response.setRecipe(map(savedEntity));
        return response;
    }

    /**
     * Deletes recipes by their IDs.
     *
     * @param ids the IDs of the recipes to delete
     * @return the response message indicating the success of the deletion
     */
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

    /**
     * Retrieves recipes by their name.
     *
     * @param searchString the name to search for
     * @return the response message containing the retrieved recipes
     */
    private RecipeResponseMessage getByName(String searchString) {
        final List<RecipeDTO> recipes = service.findByName(searchString).stream().map(this::map).toList();

        final RecipeResponseMessage response = new RecipeResponseMessage();
        response.setRecipes(recipes);
        return response;
    }

    /**
     * Retrieves recipes by the products used in them.
     *
     * @param products the product URIs to search for
     * @return the response message containing the retrieved recipes
     */
    private RecipeResponseMessage getByProduct(String[] products) {
        final List<RecipeDTO> recipes = service.findByProducts(List.of(products)).stream().map(this::map).toList();

        final RecipeResponseMessage response = new RecipeResponseMessage();
        response.setRecipes(recipes);
        return response;
    }

    /**
     * Maps a RecipeEntity to a RecipeDTO.
     *
     * @param recipeEntity the recipe entity to map
     * @return the mapped recipe DTO
     */
    private RecipeDTO map(RecipeEntity recipeEntity) {
        return new RecipeDTO(recipeEntity.getId(), recipeEntity.getTitle(), recipeEntity.getDescription(), recipeEntity.getImageUri(), recipeEntity.isPrivate(), recipeEntity.getCreationDate(), recipeEntity.getOwnerUri(), recipeEntity.getLikedByUserUris(), recipeEntity.getProductUris(), recipeEntity.getProductQuantities());
    }

    /**
     * Maps a list of Long IDs to an array of long IDs.
     *
     * @param ids the list of Long IDs to map
     * @return the array of long IDs
     */
    private long[] map(List<Long> ids) {
        return ids.stream().mapToLong(Long::longValue).toArray();
    }

    /**
     * Maps a CreateRecipeDTO to a RecipeEntity.
     *
     * @param recipe the recipe DTO to map
     * @return the mapped recipe entity
     */
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