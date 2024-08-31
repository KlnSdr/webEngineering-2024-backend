package com.sks.recipes.service.data.service;

import com.sks.recipes.service.data.entity.RecipeEntity;
import com.sks.recipes.service.data.repo.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing recipes.
 */
@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;

    /**
     * Constructs a new RecipeService with the specified RecipeRepository.
     *
     * @param recipeRepository the repository for managing recipes
     */
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    /**
     * Retrieves all recipes.
     *
     * @return a list of all recipes
     */
    public List<RecipeEntity> getAll() {
        return recipeRepository.findAll();
    }

    /**
     * Finds a recipe by its ID.
     *
     * @param id the ID of the recipe to find
     * @return an Optional containing the found recipe, or empty if not found
     */
    public Optional<RecipeEntity> findById(long id) {
        return recipeRepository.findById(id);
    }

    public List<RecipeEntity> findByOwner(String ownerUri) {
        return recipeRepository.findByOwnerUri(ownerUri);
    }

    /**
     * Deletes a recipe by its ID.
     *
     * @param id the ID of the recipe to delete
     * @return true if the recipe was successfully deleted, false otherwise
     */
    public boolean deleteById(long id) {
        recipeRepository.deleteById(id);
        return findById(id).isEmpty();
    }

    /**
     * Finds recipes by their title, ignoring case.
     *
     * @param searchString the string to search for in recipe titles
     * @return a list of recipes with titles containing the search string
     */
    public List<RecipeEntity> findByName(String searchString) {
        return recipeRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(searchString, searchString);
    }

    /**
     * Finds recipes by the URIs of products used in them.
     *
     * @param productUris the list of product URIs to search for
     * @return a list of recipes containing the specified product URIs
     */
    public List<RecipeEntity> findByProducts(List<String> productUris) {
        List<Long> recipeIds = recipeRepository.findByProductsContaining(productUris);
        return recipeRepository.findAllById(recipeIds);
    }

    /**
     * Saves a recipe.
     *
     * @param recipeEntity the recipe to save
     * @return the saved recipe
     */
    public RecipeEntity save(RecipeEntity recipeEntity) {
        return recipeRepository.save(recipeEntity);
    }
}