package com.sks.gateway.recipes.rest;

import com.sks.gateway.recipes.dto.RecipeDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RecipeSearchTest {

    private SearchResource controller;

    @BeforeEach
    public void setUp() {
        controller = new SearchResource();
    }

    @Test
    public void testSearchRecipes() {
        String query = "Käsesoße";
        List<RecipeDTO> response = controller.searchRecipes(query);

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(1, response.get(0).getId());
        assertEquals("Käsesoße", response.get(0).getTitle());
        assertEquals("https://via.placeholder.com/150", response.get(0).getImgUri());
        assertEquals("Soße aus Käse", response.get(0).getDescription());
        assertNotNull(response.get(0).getCreationDate());
        assertEquals("/users/42", response.get(0).getOwnerUri());

    }

    @Test
    public void testSearchRecipeByProducts() {
        String[] products = {"Käse", "Milch"};
        List<RecipeDTO> response = controller.searchRecipeByProducts(products);

        assertNotNull(response);
        assertEquals(3, response.size());

        }
    }
