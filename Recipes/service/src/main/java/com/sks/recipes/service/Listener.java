package com.sks.recipes.service;

import com.sks.recipes.api.RecipesListener;
import com.sks.recipes.api.RecipeRequestMessage;
import com.sks.recipes.api.RecipeResponseMessage;
import com.sks.recipes.api.RecipeSender;
import org.springframework.stereotype.Component;

@Component
public class Listener implements RecipesListener {
    private final RecipeSender sender;

    public Listener(RecipeSender sender) {
        this.sender = sender;
    }

    @Override
    public void listen(RecipeRequestMessage message) {
        sender.sendResponse(message, new RecipeResponseMessage("Listener got message: " + message.getMessage()));
    }
}
