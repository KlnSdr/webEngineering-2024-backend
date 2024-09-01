package com.sks.gateway.surveys.rest;

import com.sks.gateway.util.AccessVerifier;
import com.sks.gateway.util.UserHelper;
import com.sks.surveys.api.SurveyDTO;
import com.sks.surveys.api.SurveyRequestMessage;
import com.sks.surveys.api.SurveyResponseMessage;
import com.sks.surveys.api.SurveySender;
import com.sks.users.api.UserDTO;
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

    @Mock
    private AccessVerifier accessVerifier;

    @Mock
    private UserHelper userHelper;

    private SurveysResource controller;

    @BeforeEach
    void setUp() {
        accessVerifier = mock(AccessVerifier.class);
        sender = mock(SurveySender.class);
        userHelper = mock(UserHelper.class);
        controller = new SurveysResource(sender , accessVerifier, userHelper);
    }

    @Test
    void testGetSurveyById() {
        long userId = 1;
        int surveyId = 1;
        final UserDTO user = new UserDTO();
        user.setUserId(userId);
        SurveyResponseMessage response = mock(SurveyResponseMessage.class);
        SurveyDTO survey = new SurveyDTO();
        survey.setId(surveyId);
        survey.setTitle("Test Survey");
        survey.setParticipants(new String[] {"/users/id/1", "/users/id/2"});

        when(userHelper.getCurrentInternalUser(null)).thenReturn(user);
        when(sender.sendRequest(any(SurveyRequestMessage.class))).thenReturn(response);
        when(response.getSurveys()).thenReturn(new SurveyDTO[]{survey});

        SurveyDTO result = controller.getSurveyById(surveyId, null);

        assertNotNull(result);
        assertEquals(surveyId, result.getId());
        assertEquals("Test Survey", result.getTitle());
    }

    @Test
    void testGetSurveyByIdNotFound() {
        long userId = 1;
        int surveyId = 1;
        final UserDTO user = new UserDTO();
        user.setUserId(userId);
        SurveyResponseMessage response = mock(SurveyResponseMessage.class);

        when(userHelper.getCurrentInternalUser(null)).thenReturn(user);
        when(sender.sendRequest(any(SurveyRequestMessage.class))).thenReturn(response);
        when(response.getSurveys()).thenReturn(new SurveyDTO[]{});

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.getSurveyById(surveyId, null);
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

        when(accessVerifier.verifyAccessesSelf(userId, null)).thenReturn(true);
        when(sender.sendRequest(any(SurveyRequestMessage.class))).thenReturn(response);
        when(response.getSurveys()).thenReturn(new SurveyDTO[]{survey});

        SurveyDTO[] result = controller.getSurveysByUserId(userId,null);

        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals(1, result[0].getId());
        assertEquals("Kauf oder Verkauf", result[0].getTitle());
    }

    @Test
    void testGetSurveysByUserIdNotFound() {
        int userId = 1;
        SurveyResponseMessage response = mock(SurveyResponseMessage.class);

        when(accessVerifier.verifyAccessesSelf(userId, null)).thenReturn(true);
        when(sender.sendRequest(any(SurveyRequestMessage.class))).thenReturn(response);
        when(response.getSurveys()).thenReturn(new SurveyDTO[]{});

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.getSurveysByUserId(userId,null);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("No surveys found", exception.getReason());
    }

    @Test
    void testCreateSurvey() {
        long userId = 42;
        final UserDTO user = new UserDTO();
        user.setUserId(userId);
        SurveyDTO survey = new SurveyDTO();
        survey.setTitle("Bannenbrot");
        survey.setParticipants(new String[] {"/users/id/42", "/users/id/43"});
        survey.setOptions(List.of("/recipes/1", "recepies/2"));
        survey.setCreator("/users/id/42");
        survey.setRecipeVote(null);

        SurveyResponseMessage response = mock(SurveyResponseMessage.class);
        when(userHelper.getCurrentInternalUser(null)).thenReturn(user);
        when(sender.sendRequest(any(SurveyRequestMessage.class))).thenReturn(response);
        when(response.getSurveys()).thenReturn(new SurveyDTO[]{survey});

        SurveyDTO result = controller.createSurvey(survey, null);

        assertNotNull(result);
        assertEquals("Bannenbrot", result.getTitle());
        assertEquals(2, result.getParticipants().length);
        assertEquals("/users/id/42", result.getCreator());
        assertEquals(2, result.getOptions().size());
    }

    @Test
    void testCreateSurveyInvalid() {
        long userId = 42;
        final UserDTO user = new UserDTO();
        user.setUserId(userId);
        SurveyDTO survey = new SurveyDTO();
        survey.setTitle("");
        when(userHelper.getCurrentInternalUser(null)).thenReturn(user);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.createSurvey(survey, null);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Survey is not valid", exception.getReason());
    }

    @Test
    void testDeleteSurvey() {
        int surveyId = 1;
        final UserDTO user = new UserDTO();
        user.setUserId(42L);
        SurveyResponseMessage responseDelete = mock(SurveyResponseMessage.class);
        SurveyResponseMessage responseGet = mock(SurveyResponseMessage.class);

        final SurveyDTO survey = new SurveyDTO();
        survey.setCreator("/users/id/42");

        when(userHelper.getCurrentInternalUser(null)).thenReturn(user);
        when(sender.sendRequest(any(SurveyRequestMessage.class))).thenReturn(responseGet).thenReturn(responseDelete);
        when(responseDelete.getMessage()).thenReturn(null);
        when(responseGet.getSurveys()).thenReturn(new SurveyDTO[]{survey});

        ResponseEntity<Void> result = controller.deleteSurvey(surveyId, null);

        assertNotNull(result);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    void testDeleteSurveyBadRequest() {
        final UserDTO user = new UserDTO();
        user.setUserId(42L);
        int surveyId = 1;
        SurveyResponseMessage response = mock(SurveyResponseMessage.class);

        when(userHelper.getCurrentInternalUser(null)).thenReturn(user);
        when(sender.sendRequest(any(SurveyRequestMessage.class))).thenReturn(response);
        when(response.getMessage()).thenReturn("Survey not found");
        when(response.getSurveys()).thenReturn(new SurveyDTO[]{});

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.deleteSurvey(surveyId, null);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Survey not found", exception.getReason());
    }

    @Test
    void testUpdateSurvey() {
        final UserDTO user = new UserDTO();
        user.setUserId(42L);
        int surveyId = 1;
        SurveyDTO survey = new SurveyDTO();
        survey.setId(surveyId);
        survey.setTitle("Bannenbrot");
        survey.setParticipants(new String[] {"/users/42", "/users/43"});
        survey.setOptions(List.of("/recipes/1", "recepies/2"));
        survey.setCreator("/users/id/42");
        survey.setRecipeVote(null);

        SurveyResponseMessage responsePut = mock(SurveyResponseMessage.class);
        SurveyResponseMessage responseGet = mock(SurveyResponseMessage.class);
        final SurveyDTO existingSurvey = new SurveyDTO();
        existingSurvey.setCreator("/users/id/42");

        when(userHelper.getCurrentInternalUser(null)).thenReturn(user);
        when(sender.sendRequest(any(SurveyRequestMessage.class))).thenReturn(responseGet).thenReturn(responsePut);
        when(responsePut.getSurveys()).thenReturn(new SurveyDTO[]{survey});
        when(responseGet.getSurveys()).thenReturn(new SurveyDTO[]{existingSurvey});

        SurveyDTO result = controller.updateSurvey(surveyId, survey, null);

        assertNotNull(result);
        assertEquals(surveyId, result.getId());
        assertEquals("Bannenbrot", result.getTitle());
        assertEquals(2, result.getParticipants().length);
        assertEquals("/users/id/42", result.getCreator());
        assertEquals(2, result.getOptions().size());
    }

    @Test
    void testUpdateSurveyBadRequest() {
        final UserDTO user = new UserDTO();
        user.setUserId(42L);

        int surveyId = 1;
        SurveyDTO survey = new SurveyDTO();
        survey.setId(surveyId + 1);

        when(userHelper.getCurrentInternalUser(null)).thenReturn(user);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.updateSurvey(surveyId, survey, null);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Survey is not valid", exception.getReason());
    }

    @Test
    void testUpdateSurveyInvalid() {
        final UserDTO user = new UserDTO();
        user.setUserId(42L);

        int surveyId = 1;
        SurveyDTO survey = new SurveyDTO();
        survey.setId(surveyId);
        survey.setTitle("");

        when(userHelper.getCurrentInternalUser(null)).thenReturn(user);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.updateSurvey(surveyId, survey, null);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Survey is not valid", exception.getReason());
    }

    @Test
    void testVoteForRecipe() {
        int surveyId = 1;
        int recipeId = 1;
        int userId = 1;
        final UserDTO user = new UserDTO();
        user.setUserId(1L);

        SurveyResponseMessage response = mock(SurveyResponseMessage.class);
        SurveyDTO survey = new SurveyDTO();
        survey.setId(surveyId);
        survey.setTitle("Mein oder Dein?");
        survey.setOptions(List.of("/recipes/1", "/recipes/2"));
        survey.setRecipeVote(new HashMap<>());
        survey.getRecipeVote().put("1", userId);
        survey.setParticipants(new String[] {"/users/id/1", "/users/id/2"});

        when(userHelper.getCurrentInternalUser(null)).thenReturn(user);
        when(sender.sendRequest(any(SurveyRequestMessage.class))).thenReturn(response);
        when(response.getSurveys()).thenReturn(new SurveyDTO[]{survey});
        when(response.getMessage()).thenReturn(null);

        SurveyDTO result = controller.voteForRecipe(surveyId, recipeId, null);

        assertNotNull(result);
        assertEquals(surveyId, result.getId());
        assertEquals("Mein oder Dein?", result.getTitle());
        assertNotNull(result.getRecipeVote());
        assertEquals(1, result.getRecipeVote().size());
    }
}
