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

        when(recipeRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase("Käse", "Käse")).thenReturn(List.of(recipe1, recipe3));

        List<RecipeEntity> result = recipeService.findByName("Käse");

        assertEquals(2, result.size());
        assertEquals(recipe1, result.get(0));
        assertEquals(recipe3, result.get(1));
    }

    @Test
    void findRecipesByNameReturnsEmptyListWhenNoRecipes() {
        when(recipeRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase("Käse", "Käse")).thenReturn(Collections.emptyList());

        List<RecipeEntity> result = recipeService.findByName("Käse");

        assertEquals(0, result.size());
    }

    @Test
    void findRecipesByOneProduct() {
        RecipeEntity recipe1 =  new RecipeEntity();
        recipe1.setProductUris(List.of("/product/42", "/product/41"));
        RecipeEntity recipe3 =  new RecipeEntity();
        recipe3.setProductUris(List.of("/product/42", "/product/43"));

        when(recipeRepository.findByProductsContaining(List.of("/product/41", "/products/42", "/products/43"))).thenReturn(List.of(1L, 3L));
        when(recipeRepository.findAllById(List.of(1L, 3L))).thenReturn(List.of(recipe1, recipe3));

        List<RecipeEntity> result = recipeService.findByProducts(List.of("/product/41", "/products/42", "/products/43"));

        assertEquals(2, result.size());
        assertEquals(recipe1, result.get(0));
        assertEquals(recipe3, result.get(1));
    }

    @Test
    void findRecipesByProductsReturnsEmptyListWhenNoRecipes() {
        when(recipeRepository.findByProductsContaining(List.of("/product/42"))).thenReturn(Collections.emptyList());

        List<RecipeEntity> result = recipeService.findByProducts(List.of("/product/42"));

        assertEquals(0, result.size());
    }

    @Test
    void findRecipesByMultipleProducts() {
        RecipeEntity recipe1 =  new RecipeEntity();
        recipe1.setProductUris(List.of("/product/42", "/product/41"));

        when(recipeRepository.findByProductsContaining(List.of("/product/42", "/product/41"))).thenReturn(List.of(1L));
        when(recipeRepository.findAllById(List.of(1L))).thenReturn(List.of(recipe1));

        List<RecipeEntity> result = recipeService.findByProducts(List.of("/product/42", "/product/41"));

        assertEquals(1, result.size());
        assertEquals(recipe1, result.getFirst());
    }
    @Test
    void dontfindRecipesMissingProducts() {
        RecipeEntity recipe1 = new RecipeEntity();
        recipe1.setProductUris(List.of("/product/42", "/product/41", "/product/40", "/product/43", "/product/44"));
        when(recipeRepository.findByProductsContaining(List.of("/product/42", "/product/41", "/product/40", "/product/43","/product/44"))).thenReturn(List.of(1L));
        when(recipeRepository.findAllById(List.of(1L))).thenReturn(List.of(recipe1));

        List<RecipeEntity> result = recipeService.findByProducts(List.of("/product/42", "/product/41", "/product/40", "/product/43"));

        assertEquals(0, result.size());


    }

    @Test
    void findByOwnerReturnsListOfRecipes() {
        RecipeEntity recipe = new RecipeEntity();
        when(recipeRepository.findByOwnerUri("/users/42")).thenReturn(List.of(recipe));

        List<RecipeEntity> result = recipeService.findByOwner("/users/42");

        assertEquals(1, result.size());
        assertEquals(recipe, result.getFirst());
    }

    @Test
    void findByOwnerReturnsEmptyListWhenNoRecipes() {
        when(recipeRepository.findByOwnerUri("/users/42")).thenReturn(Collections.emptyList());

        List<RecipeEntity> result = recipeService.findByOwner("/users/42");

        assertEquals(0, result.size());
    }
}