package com.sks.gateway.recipes.rest;

import com.sks.recipes.api.dto.CreateRecipeDTO;
import com.sks.recipes.api.dto.RecipeDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RecipesResourceTest {
    private RecipesResource controller;

    @BeforeEach
    public void setUp() {
        controller = new RecipesResource();
    }

    @Test
    public void testGetRecipeById() {
        int id = 1;
        RecipeDTO response = controller.getRecipeById(id);

        assertNotNull(response);
        assertEquals(id, response.getId());
        assertEquals("Flammkuchen", response.getTitle());
        assertEquals("Flammkuchen ist ein dünner Fladenbrotteig, der mit Crème fraîche, Zwiebeln und Speck belegt wird...", response.getDescription());
        assertEquals("/static/images/695f6e65b4bcd2cd19c7b0dd62b0fb82.png", response.getImgUri());
        assertNotNull(response.getCreationDate());
        assertEquals("/users/42", response.getOwnerUri());
    }

    @Test
    public void testGetMultipleRecipesById() {
        int[] ids = {1, 2, 3};
        RecipeDTO[] responses = controller.getMultipleRecipesById(ids);

        assertNotNull(responses);
        assertEquals(ids.length, responses.length);

        for (int i = 0; i < ids.length; i++) {
            assertNotNull(responses[i]);
            assertEquals(ids[i], responses[i].getId());
            assertEquals("Flammkuchen", responses[i].getTitle());
            assertEquals("Flammkuchen ist ein dünner Fladenbrotteig, der mit Crème fraîche, Zwiebeln und Speck belegt wird...", responses[i].getDescription());
            assertEquals("/static/images/695f6e65b4bcd2cd19c7b0dd62b0fb82.png", responses[i].getImgUri());
            assertNotNull(responses[i].getCreationDate());
            assertEquals("/users/42", responses[i].getOwnerUri());
        }
    }

    @Test
    public void testCreateRecipe() {
        CreateRecipeDTO createRecipeDTO = new CreateRecipeDTO("Flammkuchen", "Flammkuchen ist ein dünner Fladenbrotteig, der mit Crème fraîche, Zwiebeln und Speck belegt wird...","/static/images/695f6e65b4bcd2cd19c7b0dd62b0fb82.png", "/users/42");
        RecipeDTO response = controller.createRecipe(createRecipeDTO);

        assertNotNull(response);
        assertEquals(42, response.getId());
        assertEquals(createRecipeDTO.getTitle(), response.getTitle());
        assertEquals(createRecipeDTO.getImgUri(), response.getImgUri());
        assertEquals(createRecipeDTO.getDescription(), response.getDescription());
        assertNotNull(response.getCreationDate());
        assertEquals(createRecipeDTO.getOwnerUri(), response.getOwnerUri());
    }

    @Test
    public void testUpdateRecipe() {
        int id = 1;
        RecipeDTO updateRecipeDTO = new RecipeDTO(id, "Flammkuchen Updated", "Updated Description","/static/images/updated.png",new Date(), "/users/42");
        RecipeDTO response = controller.updateRecipe(id, updateRecipeDTO);

        assertNotNull(response);
        assertEquals(updateRecipeDTO.getId(), response.getId());
        assertEquals(updateRecipeDTO.getTitle(), response.getTitle());
        assertEquals(updateRecipeDTO.getImgUri(), response.getImgUri());
        assertEquals(updateRecipeDTO.getDescription(), response.getDescription());
        assertEquals(updateRecipeDTO.getCreationDate(), response.getCreationDate());
        assertEquals(updateRecipeDTO.getOwnerUri(), response.getOwnerUri());
    }

    @Test
    public void testDeleteRecipe() {
        int id = 1;
        ResponseEntity<Void> response = controller.deleteRecipe(id);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
