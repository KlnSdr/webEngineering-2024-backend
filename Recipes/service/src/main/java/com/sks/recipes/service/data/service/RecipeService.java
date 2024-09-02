package com.sks.recipes.service.data.service;

import com.sks.recipes.service.data.entity.RecipeEntity;
import com.sks.recipes.service.data.repo.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public List<RecipeEntity> getAll() {
        return recipeRepository.findAll();
    }

    public Optional<RecipeEntity> findById(long id) {
        return recipeRepository.findById(id);
    }

    public boolean deleteById(long id) {
        recipeRepository.deleteById(id);

        return findById(id).isEmpty();
    }

    public List<RecipeEntity> findByName(String searchString) {
        return recipeRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(searchString);
    }
    public List<RecipeEntity> findByProducts(List<String> productUris) {
        return recipeRepository.findByProductUrisIn(productUris);
    }

    public RecipeEntity save(RecipeEntity recipeEntity) {
        return recipeRepository.save(recipeEntity);
    }
}
