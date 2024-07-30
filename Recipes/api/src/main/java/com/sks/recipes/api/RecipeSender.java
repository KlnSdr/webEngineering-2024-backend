package com.sks.recipes.api;

import com.sks.base.api.BaseSender;

public interface RecipeSender extends BaseSender<RecipeRequestMessage, RecipeResponseMessage> {
    RecipeResponseMessage sendRequest(RecipeRequestMessage message);
    void sendResponse(RecipeRequestMessage request, RecipeResponseMessage response);
}
