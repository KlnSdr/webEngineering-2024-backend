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

    @Test
    void findRecipesByName() {
        RecipeEntity recipe1 =  new RecipeEntity();
        recipe1.setTitle("Käsesoße");
        RecipeEntity recipe2 =  new RecipeEntity();
        recipe2.setTitle("Tomatensoße");
        RecipeEntity recipe3 =  new RecipeEntity();
        recipe3.setTitle("Käsesosse");

        when(recipeRepository.findByTitleContainingIgnoreCase("Käse")).thenReturn(List.of(recipe1, recipe3));

        List<RecipeEntity> result = recipeService.findRecipesByName("Käse");

        assertEquals(2, result.size());
        assertEquals(recipe1, result.get(0));
        assertEquals(recipe3, result.get(1));
    }

    @Test
    void findRecipesByNameReturnsEmptyListWhenNoRecipes() {
        when(recipeRepository.findByTitleContainingIgnoreCase("Käse")).thenReturn(Collections.emptyList());

        List<RecipeEntity> result = recipeService.findRecipesByName("Käse");

        assertEquals(0, result.size());
    }

    @Test
    void findRecipesByOneProduct() {
        RecipeEntity recipe1 =  new RecipeEntity();
        recipe1.setProductUris(List.of("Käse", "Sauerteig"));
        RecipeEntity recipe2 =  new RecipeEntity();
        recipe2.setProductUris(List.of("Apfel", "Sauerteig"));
        RecipeEntity recipe3 =  new RecipeEntity();
        recipe3.setProductUris(List.of("Käse", "Milch"));

        when(recipeRepository.findByProductUrisIn(Collections.singleton(List.of("Käse")))).thenReturn(List.of(recipe1, recipe3));

        List<RecipeEntity> result = recipeService.findRecipesByProducts(List.of("Käse"));

        assertEquals(2, result.size());
        assertEquals(recipe1, result.get(0));
        assertEquals(recipe3, result.get(1));
    }

    @Test
    void findRecipesByProductsReturnsEmptyListWhenNoRecipes() {
        when(recipeRepository.findByProductUrisIn(Collections.singleton(List.of("Käse")))).thenReturn(Collections.emptyList());

        List<RecipeEntity> result = recipeService.findRecipesByProducts(List.of("Käse"));

        assertEquals(0, result.size());
    }

    @Test
    void findRecipesByMultipleProducts() {
        RecipeEntity recipe1 =  new RecipeEntity();
        recipe1.setProductUris(List.of("Käse", "Sauerteig"));
        RecipeEntity recipe2 =  new RecipeEntity();
        recipe2.setProductUris(List.of("Apfel", "Sauerteig"));
        RecipeEntity recipe3 =  new RecipeEntity();
        recipe3.setProductUris(List.of("Käse", "Milch"));

        when(recipeRepository.findByProductUrisIn(Collections.singleton(List.of("Käse", "Sauerteig")))).thenReturn(List.of(recipe1));

        List<RecipeEntity> result = recipeService.findRecipesByProducts(List.of("Käse", "Sauerteig"));

        assertEquals(1, result.size());
        assertEquals(recipe1, result.get(0));
    }
}