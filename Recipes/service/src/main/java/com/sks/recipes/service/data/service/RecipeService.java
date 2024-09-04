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

    public List<RecipeEntity> findByOwner(String ownerUri) {
        return recipeRepository.findByOwnerUri(ownerUri);
    }

    public boolean deleteById(long id) {
        recipeRepository.deleteById(id);

        return findById(id).isEmpty();
    }

    public List<RecipeEntity> findByName(String searchString) {
        return recipeRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(searchString, searchString);
    }
    public List<RecipeEntity> findByProducts(List<String> productUris) {
        List<Long> recipeIds = recipeRepository.findByProductsContaining(productUris);
        return recipeRepository.findAllById(recipeIds);
    }

    public RecipeEntity save(RecipeEntity recipeEntity) {
        return recipeRepository.save(recipeEntity);
    }
}
