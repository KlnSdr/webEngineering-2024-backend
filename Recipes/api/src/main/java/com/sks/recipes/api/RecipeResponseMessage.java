package com.sks.recipes.api;

import com.sks.base.api.BaseMessage;
import com.sks.recipes.api.dto.RecipeDTO;

import java.util.List;


public class RecipeResponseMessage extends BaseMessage {
    private List<RecipeDTO> recipes;
    private RecipeDTO recipe;
    private boolean wasSuccessful;
    private long[] notDeletedIds;

    public RecipeResponseMessage() {
    }

    public RecipeResponseMessage(List<RecipeDTO> recipes) {
        this.recipes = recipes;
    }

    public List<RecipeDTO> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<RecipeDTO> recipes) {
        this.recipes = recipes;
    }

    public boolean isWasSuccessful() {
        return wasSuccessful;
    }

    public void setWasSuccessful(boolean wasSuccessful) {
        this.wasSuccessful = wasSuccessful;
    }

    public long[] getNotDeletedIds() {
        return notDeletedIds;
    }

    public void setNotDeletedIds(long[] notDeletedIds) {
        this.notDeletedIds = notDeletedIds;
    }

    public RecipeDTO getRecipe() {
        return recipe;
    }

    public void setRecipe(RecipeDTO recipe) {
        this.recipe = recipe;
    }
}
