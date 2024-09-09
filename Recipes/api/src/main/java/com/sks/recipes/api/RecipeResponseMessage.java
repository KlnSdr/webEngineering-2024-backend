package com.sks.recipes.api;

import com.sks.base.api.BaseMessage;
import com.sks.recipes.api.dto.RecipeDTO;

import java.util.List;

/**
 * Represents a response message for recipe-related operations.
 */
public class RecipeResponseMessage extends BaseMessage {
    private List<RecipeDTO> recipes;
    private RecipeDTO recipe;
    private boolean wasSuccessful;
    private long[] notDeletedIds;

    /**
     * Default constructor.
     */
    public RecipeResponseMessage() {
    }

    /**
     * Constructor with a list of recipes.
     *
     * @param recipes the list of recipes
     */
    public RecipeResponseMessage(List<RecipeDTO> recipes) {
        this.recipes = recipes;
    }

    /**
     * Gets the list of recipes.
     *
     * @return the list of recipes
     */
    public List<RecipeDTO> getRecipes() {
        return recipes;
    }

    /**
     * Sets the list of recipes.
     *
     * @param recipes the list of recipes to set
     */
    public void setRecipes(List<RecipeDTO> recipes) {
        this.recipes = recipes;
    }

    /**
     * Checks if the operation was successful.
     *
     * @return true if the operation was successful, false otherwise
     */
    public boolean isWasSuccessful() {
        return wasSuccessful;
    }

    /**
     * Sets the success status of the operation.
     *
     * @param wasSuccessful the success status to set
     */
    public void setWasSuccessful(boolean wasSuccessful) {
        this.wasSuccessful = wasSuccessful;
    }

    /**
     * Gets the IDs of recipes that were not deleted.
     *
     * @return the IDs of recipes that were not deleted
     */
    public long[] getNotDeletedIds() {
        return notDeletedIds;
    }

    /**
     * Sets the IDs of recipes that were not deleted.
     *
     * @param notDeletedIds the IDs to set
     */
    public void setNotDeletedIds(long[] notDeletedIds) {
        this.notDeletedIds = notDeletedIds;
    }

    /**
     * Gets a single recipe.
     *
     * @return the recipe
     */
    public RecipeDTO getRecipe() {
        return recipe;
    }

    /**
     * Sets a single recipe.
     *
     * @param recipe the recipe to set
     */
    public void setRecipe(RecipeDTO recipe) {
        this.recipe = recipe;
    }
}