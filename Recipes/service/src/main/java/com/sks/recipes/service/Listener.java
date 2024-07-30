package com.sks.recipes.service;

import com.sks.recipes.api.RecipesListener;
import com.sks.recipes.service.data.DemoEntity;
import com.sks.recipes.service.data.DemoRepository;
import com.sks.recipes.api.RecipeRequestMessage;
import com.sks.recipes.api.RecipeResponseMessage;
import com.sks.recipes.api.RecipeSender;
import org.springframework.stereotype.Component;

@Component
public class Listener implements RecipesListener {
    private final RecipeSender sender;
    private final DemoRepository repo;

    public Listener(RecipeSender sender, DemoRepository repo) {
        this.sender = sender;
        this.repo = repo;
    }

    @Override
    public void listen(RecipeRequestMessage message) {
        final DemoEntity entity = new DemoEntity();
        entity.setName(message.getMessage());
        repo.save(entity);

        repo.findAll().forEach(demoEntity -> System.out.println(demoEntity.getName()));
        sender.sendResponse(message, new RecipeResponseMessage("Listener got message: " + message.getMessage()));
    }
}
