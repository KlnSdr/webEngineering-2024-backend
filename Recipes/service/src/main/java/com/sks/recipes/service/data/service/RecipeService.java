package com.sks.recipes.service.data.service;

import com.sks.recipes.service.data.entity.RecipeEntity;
import com.sks.recipes.service.data.repo.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public List<RecipeEntity> getAll() {
        return recipeRepository.findAll();
    }

    public List<RecipeEntity> findByName(String searchString) {
        return recipeRepository.findByTitleContainingIgnoreCase(searchString);
    }
    public List<RecipeEntity> findByProducts(List<String> productUris) {
        return recipeRepository.findByProductUrisIn(Collections.singleton(productUris));
    }

    public RecipeEntity save(RecipeEntity recipeEntity) {
        return recipeRepository.save(recipeEntity);
    }
}
