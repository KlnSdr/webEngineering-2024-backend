package com.sks.recipes.api;

import com.sks.base.api.BaseMessage;
import com.sks.recipes.api.dto.RecipeDTO;

import java.util.List;


public class RecipeRequestMessage extends BaseMessage {
    private String message;
    private String[] products = new String[0];
    private List<RecipeDTO> recipes;
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

    public List<RecipeDTO> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<RecipeDTO> recipes) {
        this.recipes = recipes;
    }

    public RecipeRequestType getRequestType() {
        return requestType;
    }
}
