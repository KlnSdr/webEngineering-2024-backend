package com.sks.recipes.service.data.service;

import com.sks.recipes.service.data.entity.RecipeEntity;
import com.sks.recipes.service.data.repo.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private RecipeService recipeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllReturnsListOfRecipes() {
        RecipeEntity recipe = new RecipeEntity();
        when(recipeRepository.findAll()).thenReturn(List.of(recipe));

        List<RecipeEntity> result = recipeService.getAll();

        assertEquals(1, result.size());
        assertEquals(recipe, result.getFirst());
    }

    @Test
    void getAllReturnsEmptyListWhenNoRecipes() {
        when(recipeRepository.findAll()).thenReturn(Collections.emptyList());

        List<RecipeEntity> result = recipeService.getAll();

        assertEquals(0, result.size());
    }

    @Test
    void saveReturnsSavedRecipe() {
        RecipeEntity recipe = new RecipeEntity();
        when(recipeRepository.save(recipe)).thenReturn(recipe);

        RecipeEntity result = recipeService.save(recipe);

        assertEquals(recipe, result);
    }
}