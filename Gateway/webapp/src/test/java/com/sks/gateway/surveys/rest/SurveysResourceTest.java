package com.sks.gateway.surveys.rest;

import com.sks.recipes.api.dto.RecipeDTO;
import com.sks.surveys.api.SurveyDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SurveysResourceTest {
    private SurveysResource controller;

    @BeforeEach
    public void setUp() {
        controller = new SurveysResource();
    }

    @Test
    public void testGetSurveyById() {
        int id = 1;
        SurveyDTO response = controller.getSurveyById(id);

        assertNotNull(response);
        assertEquals(id, response.getId());
        assertEquals("Survey", response.getTitle());
        assertEquals("/users/42", response.getCreator());
        assertNotNull(response.getCreationDate());
    }

    @Test
    public void testGetSurveysByUserId() {
        int userId = 42;
        SurveyDTO[] responses = controller.getSurveysByUserId(userId);

        assertNotNull(responses);
        assertEquals(2, responses.length);

    }

    @Test
    public void testCreateSurvey() {
        SurveyDTO survey = new SurveyDTO(1, "Survey", new String[]{"/users/41", "/users/40"}, "/users/42",  Map.of(new RecipeDTO(), 0), Date.from(Instant.now()));
        SurveyDTO response = controller.createSurvey(survey);

        assertNotNull(response);
        assertEquals(42, response.getId());
        assertEquals("Survey", response.getTitle());
        assertEquals("/users/42", response.getCreator());
        assertEquals(1, response.getRecipeVote().size());
        assertNotNull(response.getCreationDate());
    }

    @Test
    public void testDeleteSurvey() {
        int id = 1;
        ResponseEntity<Void> response = controller.deleteSurvey(id);
    }

    @Test
    public void testUpdateSurvey() {
        int id = 1;
        SurveyDTO survey = new SurveyDTO(1, "Survey", new String[]{"/users/41", "/users/40"}, "/users/42",  Map.of(new RecipeDTO(), 0), Date.from(Instant.now()));
        SurveyDTO response = controller.updateSurvey(id, survey);

        assertNotNull(response);
        assertEquals(id, response.getId());
        assertEquals("Survey", response.getTitle());
        assertEquals("/users/42", response.getCreator());
        assertEquals(1, response.getRecipeVote().size());
        assertNotNull(response.getCreationDate());
    }

    @Test
    public void testVoteForRecipe() {
        int id = 1;
        int recipeId = 1;
        SurveyDTO response = controller.voteForRecipe(id, recipeId);

        assertNotNull(response);
        assertEquals(id, response.getId());
        assertEquals("Survey", response.getTitle());
        assertEquals("/users/42", response.getCreator());
        assertEquals(1, response.getRecipeVote().size());
        assertNotNull(response.getCreationDate());
    }


}
