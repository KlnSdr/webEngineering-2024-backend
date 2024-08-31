package com.sks.recipes.api;

import com.sks.base.api.BaseSender;

/**
 * Interface for sending recipe-related requests and responses.
 */
public interface RecipeSender extends BaseSender<RecipeRequestMessage, RecipeResponseMessage> {

    /**
     * Sends a recipe request message and returns the response.
     *
     * @param message the recipe request message
     * @return the recipe response message
     */
    RecipeResponseMessage sendRequest(RecipeRequestMessage message);

    /**
     * Sends a recipe response message for a given request.
     *
     * @param request the recipe request message
     * @param response the recipe response message
     */
    void sendResponse(RecipeRequestMessage request, RecipeResponseMessage response);
}