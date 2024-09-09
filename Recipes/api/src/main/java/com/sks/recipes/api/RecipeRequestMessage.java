package com.sks.recipes.api;

import com.sks.base.api.BaseMessage;
import com.sks.recipes.api.dto.CreateRecipeDTO;

/**
 * Class representing a request message for recipes.
 */
public class RecipeRequestMessage extends BaseMessage {
    private String message;
    private String[] products = new String[0];
    private RecipeRequestType requestType;
    private long[] ids;
    private CreateRecipeDTO recipe;
    private long ownerId;

    /**
     * Default constructor for RecipeRequestMessage.
     */
    public RecipeRequestMessage() {
    }

    /**
     * Constructs a RecipeRequestMessage for searching by name.
     *
     * @param searchString the search string to use
     */
    public RecipeRequestMessage(String searchString) {
        this.message = searchString;
        this.requestType = RecipeRequestType.SEARCH_BY_NAME;
    }

    /**
     * Constructs a RecipeRequestMessage for searching by products.
     *
     * @param products the array of product names to search by
     */
    public RecipeRequestMessage(String[] products) {
        this.products = products;
        this.requestType = RecipeRequestType.SEARCH_BY_PRODUCTS;
    }

    /**
     * Creates a RecipeRequestMessage for getting a recipe by ID.
     *
     * @param id the ID of the recipe to get
     * @return a new RecipeRequestMessage instance
     */
    public static RecipeRequestMessage getById(long id) {
        final RecipeRequestMessage message = new RecipeRequestMessage();
        message.ids = new long[]{id};
        message.requestType = RecipeRequestType.GET_BY_ID;
        return message;
    }

    /**
     * Creates a RecipeRequestMessage for getting recipes by multiple IDs.
     *
     * @param ids the array of recipe IDs to get
     * @return a new RecipeRequestMessage instance
     */
    public static RecipeRequestMessage getById(long[] ids) {
        final RecipeRequestMessage message = new RecipeRequestMessage();
        message.ids = ids;
        message.requestType = RecipeRequestType.GET_BY_ID;
        return message;
    }

    /**
     * Creates a RecipeRequestMessage for updating a recipe.
     *
     * @param recipe the CreateRecipeDTO object containing the recipe data
     * @return a new RecipeRequestMessage instance
     */
    public static RecipeRequestMessage update(CreateRecipeDTO recipe) {
        final RecipeRequestMessage message = new RecipeRequestMessage();
        message.recipe = recipe;
        message.requestType = RecipeRequestType.UPDATE;
        return message;
    }

    /**
     * Creates a RecipeRequestMessage for deleting a recipe by ID.
     *
     * @param id the ID of the recipe to delete
     * @return a new RecipeRequestMessage instance
     */
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

    /**
     * Gets the message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message.
     *
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the products.
     *
     * @return the array of products
     */
    public String[] getProducts() {
        return products;
    }

    /**
     * Sets the products.
     *
     * @param products the array of products to set
     */
    public void setProducts(String[] products) {
        this.products = products;
    }

    /**
     * Gets the request type.
     *
     * @return the request type
     */
    public RecipeRequestType getRequestType() {
        return requestType;
    }

    /**
     * Sets the request type.
     *
     * @param requestType the request type to set
     */
    public void setRequestType(RecipeRequestType requestType) {
        this.requestType = requestType;
    }

    /**
     * Gets the IDs.
     *
     * @return the array of IDs
     */
    public long[] getIds() {
        return ids;
    }

    /**
     * Sets the IDs.
     *
     * @param ids the array of IDs to set
     */
    public void setIds(long[] ids) {
        this.ids = ids;
    }

    /**
     * Gets the recipe.
     *
     * @return the CreateRecipeDTO object
     */
    public CreateRecipeDTO getRecipe() {
        return recipe;
    }

    /**
     * Sets the recipe.
     *
     * @param recipe the CreateRecipeDTO object to set
     */
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