package com.sks.recipes.api;

import com.sks.base.api.BaseMessage;
import com.sks.recipes.api.dto.CreateRecipeDTO;


public class RecipeRequestMessage extends BaseMessage {
    private String message;
    private String[] products = new String[0];
    private RecipeRequestType requestType;
    private long[] ids;
    private CreateRecipeDTO recipe;
    private long ownerId;

    public RecipeRequestMessage() {
    }

    public RecipeRequestMessage(String searchString) {
        this.message = searchString;
        this.requestType = RecipeRequestType.SEARCH_BY_NAME;
    }

    public RecipeRequestMessage(String[] products) {
        this.products = products;
        this.requestType = RecipeRequestType.SEARCH_BY_PRODUCTS;
    }

    public static RecipeRequestMessage getById(long id) {
        final RecipeRequestMessage message = new RecipeRequestMessage();
        message.ids = new long[]{id};
        message.requestType = RecipeRequestType.GET_BY_ID;
        return message;
    }

    public static RecipeRequestMessage getById(long[] ids) {
        final RecipeRequestMessage message = new RecipeRequestMessage();
        message.ids = ids;
        message.requestType = RecipeRequestType.GET_BY_ID;
        return message;
    }

    public static RecipeRequestMessage update(CreateRecipeDTO recipe) {
        final RecipeRequestMessage message = new RecipeRequestMessage();
        message.recipe = recipe;
        message.requestType = RecipeRequestType.UPDATE;
        return message;
    }

    public static RecipeRequestMessage delete(long id) {
        final RecipeRequestMessage message = new RecipeRequestMessage();
        message.ids = new long[]{id};
        message.requestType = RecipeRequestType.DELETE;
        return message;
    }

    public static RecipeRequestMessage getByOwnerId(long ownerId) {
        final RecipeRequestMessage message = new RecipeRequestMessage();
        message.ownerId = ownerId;
        message.requestType = RecipeRequestType.GET_BY_OWNER_ID;
        return message;
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

    public RecipeRequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RecipeRequestType requestType) {
        this.requestType = requestType;
    }

    public long[] getIds() {
        return ids;
    }

    public void setIds(long[] ids) {
        this.ids = ids;
    }

    public CreateRecipeDTO getRecipe() {
        return recipe;
    }

    public void setRecipe(CreateRecipeDTO recipe) {
        this.recipe = recipe;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }
}
