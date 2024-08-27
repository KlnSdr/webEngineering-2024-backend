package com.sks.recipes.api;

import com.sks.base.api.BaseMessage;
import com.sks.recipes.api.dto.RecipeDTO;

import java.util.List;


public class RecipeResponseMessage extends BaseMessage {
    private String message;
    private List<RecipeDTO> recipes;


    public RecipeResponseMessage() {
    }

    public RecipeResponseMessage(List<RecipeDTO> recipes) {
        this.recipes = recipes;
    }

    public RecipeResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<RecipeDTO> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<RecipeDTO> recipes) {
        this.recipes = recipes;
    }

}
