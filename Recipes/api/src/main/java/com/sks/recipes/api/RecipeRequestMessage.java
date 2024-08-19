package com.sks.recipes.api;

import com.sks.base.api.BaseMessage;
import com.sks.gateway.recipes.dto.RecipeDTO;

public class RecipeRequestMessage extends BaseMessage {
    private String message;
    private String[] products = new String[0];
    private RecipeDTO[] recipes = new RecipeDTO[0];
    private RecipeRequestType requestType;

    public RecipeRequestMessage() {
    }

    public RecipeRequestMessage(String searchString) {
        this.message = searchString;
        this.requestType = RecipeRequestType.ByName;
    }

    public RecipeRequestMessage(String[] products) {
        this.products = products;
        this.requestType = RecipeRequestType.ByPRODUCT;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String[] getProducts() {
        return products;
    }

    public void setProducts(String[] products) {
        this.products = products;
    }

    public RecipeDTO[] getRecipes() {
        return recipes;
    }

    public void setRecipes(RecipeDTO[] recipes) {
        this.recipes = recipes;
    }

    public RecipeRequestType getRequestType() {
        return requestType;
    }
}
