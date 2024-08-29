package com.sks.gateway.surveys.rest;

import com.sks.surveys.api.SurveyDTO;
import com.sks.surveys.api.SurveyRequestMessage;
import com.sks.surveys.api.SurveyResponseMessage;
import com.sks.surveys.api.SurveySender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SurveysResourceTest {
    @Mock
    private SurveySender sender;

    private SurveysResource controller;

    @BeforeEach
    void setUp() {
        sender = mock(SurveySender.class);
        controller = new SurveysResource(sender);
    }

    @Test
    void testGetSurveyById() {
        int surveyId = 1;
        SurveyResponseMessage response = mock(SurveyResponseMessage.class);
        SurveyDTO survey = new SurveyDTO();
        survey.setId(surveyId);
        survey.setTitle("Test Survey");

        when(sender.sendRequest(any(SurveyRequestMessage.class))).thenReturn(response);
        when(response.getSurvey()).thenReturn(new SurveyDTO[]{survey});

        SurveyDTO result = controller.getSurveyById(surveyId);

        assertNotNull(result);
        assertEquals(surveyId, result.getId());
        assertEquals("Test Survey", result.getTitle());
    }

    @Test
    void testGetSurveyByIdNotFound() {
        int surveyId = 1;
        SurveyResponseMessage response = mock(SurveyResponseMessage.class);

        when(sender.sendRequest(any(SurveyRequestMessage.class))).thenReturn(response);
        when(response.getSurvey()).thenReturn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.getSurveyById(surveyId);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Survey not found", exception.getReason());
    }

    @Test
    void testGetSurveysByUserId() {
        int userId = 1;
        SurveyResponseMessage response = mock(SurveyResponseMessage.class);
        SurveyDTO survey = new SurveyDTO();
        survey.setId(1);
        survey.setTitle("Kauf oder Verkauf");

        when(sender.sendRequest(any(SurveyRequestMessage.class))).thenReturn(response);
        when(response.getSurvey()).thenReturn(new SurveyDTO[]{survey});

        SurveyDTO[] result = controller.getSurveysByUserId(userId);

        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals(1, result[0].getId());
        assertEquals("Kauf oder Verkauf", result[0].getTitle());
    }

    @Test
    void testGetSurveysByUserIdNotFound() {
        int userId = 1;
        SurveyResponseMessage response = mock(SurveyResponseMessage.class);

        when(sender.sendRequest(any(SurveyRequestMessage.class))).thenReturn(response);
        when(response.getSurvey()).thenReturn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.getSurveysByUserId(userId);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("No surveys found", exception.getReason());
    }

    @Test
    void testCreateSurvey() {
        SurveyDTO survey = new SurveyDTO();
        survey.setTitle("Bannenbrot");
        survey.setParticipants(new String[] {"/users/42", "/users/43"});
        survey.setOptions(List.of("/recipes/1", "recepies/2"));
        survey.setCreator("/users/42");
        survey.setRecipeVote(null);

        SurveyResponseMessage response = mock(SurveyResponseMessage.class);
        when(sender.sendRequest(any(SurveyRequestMessage.class))).thenReturn(response);
        when(response.getSurvey()).thenReturn(new SurveyDTO[]{survey});

        SurveyDTO result = controller.createSurvey(survey);

        assertNotNull(result);
        assertEquals("Bannenbrot", result.getTitle());
        assertEquals(2, result.getParticipants().length);
        assertEquals("/users/42", result.getCreator());
        assertEquals(2, result.getOptions().size());
    }

    @Test
    void testCreateSurveyInvalid() {
        SurveyDTO survey = new SurveyDTO();
        survey.setTitle("");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.createSurvey(survey);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Survey is not valid", exception.getReason());
    }

    @Test
    void testDeleteSurvey() {
        int surveyId = 1;
        SurveyResponseMessage response = mock(SurveyResponseMessage.class);

        when(sender.sendRequest(any(SurveyRequestMessage.class))).thenReturn(response);
        when(response.getMessage()).thenReturn(null);

        ResponseEntity<Void> result = controller.deleteSurvey(surveyId);

        assertNotNull(result);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    void testDeleteSurveyBadRequest() {
        int surveyId = 1;
        SurveyResponseMessage response = mock(SurveyResponseMessage.class);

        when(sender.sendRequest(any(SurveyRequestMessage.class))).thenReturn(response);
        when(response.getMessage()).thenReturn("Survey not found");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.deleteSurvey(surveyId);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Survey not found", exception.getReason());
    }

    @Test
    void testUpdateSurvey() {
        int surveyId = 1;
        SurveyDTO survey = new SurveyDTO();
        survey.setId(surveyId);
        survey.setTitle("Bannenbrot");
        survey.setParticipants(new String[] {"/users/42", "/users/43"});
        survey.setOptions(List.of("/recipes/1", "recepies/2"));
        survey.setCreator("/users/42");
        survey.setRecipeVote(null);

        SurveyResponseMessage response = mock(SurveyResponseMessage.class);
        when(sender.sendRequest(any(SurveyRequestMessage.class))).thenReturn(response);
        when(response.getSurvey()).thenReturn(new SurveyDTO[]{survey});

        SurveyDTO result = controller.updateSurvey(surveyId, survey);

        assertNotNull(result);
        assertEquals(surveyId, result.getId());
        assertEquals("Bannenbrot", result.getTitle());
        assertEquals(2, result.getParticipants().length);
        assertEquals("/users/42", result.getCreator());
        assertEquals(2, result.getOptions().size());
    }

    @Test
    void testUpdateSurveyBadRequest() {
        int surveyId = 1;
        SurveyDTO survey = new SurveyDTO();
        survey.setId(surveyId + 1);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.updateSurvey(surveyId, survey);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Survey id does not match", exception.getReason());
    }

    @Test
    void testUpdateSurveyInvalid() {
        int surveyId = 1;
        SurveyDTO survey = new SurveyDTO();
        survey.setId(surveyId);
        survey.setTitle("");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.updateSurvey(surveyId, survey);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Survey is not valid", exception.getReason());
    }

    @Test
    void testVoteForRecipe() {
        int surveyId = 1;
        int recipeId = 1;
        int userId = 1;
        SurveyResponseMessage response = mock(SurveyResponseMessage.class);
        SurveyDTO survey = new SurveyDTO();
        survey.setId(surveyId);
        survey.setTitle("Mein oder Dein?");
        survey.setOptions(List.of("/recipes/1", "/recipes/2"));
        survey.setRecipeVote(new HashMap<>());
        survey.getRecipeVote().put("1", userId);

        when(sender.sendRequest(any(SurveyRequestMessage.class))).thenReturn(response);
        when(response.getSurvey()).thenReturn(new SurveyDTO[]{survey});
        when(response.getMessage()).thenReturn(null);

        SurveyDTO result = controller.voteForRecipe(surveyId, recipeId, userId);

        assertNotNull(result);
        assertEquals(surveyId, result.getId());
        assertEquals("Mein oder Dein?", result.getTitle());
        assertNotNull(result.getRecipeVote());
        assertEquals(1, result.getRecipeVote().size());
    }
}



