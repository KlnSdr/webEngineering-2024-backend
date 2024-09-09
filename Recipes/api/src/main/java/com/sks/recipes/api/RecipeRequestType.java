package com.sks.recipes.api;

/**
 * Enum representing the types of requests that can be made for recipes.
 */
public enum RecipeRequestType {
    /**
     * Request to get a recipe by its ID.
     */
    GET_BY_ID,
    GET_BY_OWNER_ID,

    /**
     * Request to update an existing recipe.
     */
    UPDATE,

    /**
     * Request to delete a recipe.
     */
    DELETE,

    /**
     * Request to search for recipes by their name.
     */
    SEARCH_BY_NAME,

    /**
     * Request to search for recipes by the products used in them.
     */
    SEARCH_BY_PRODUCTS
}