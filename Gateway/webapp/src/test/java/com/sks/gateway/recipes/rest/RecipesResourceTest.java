package com.sks.gateway.recipes.rest;

import com.sks.gateway.common.MessageErrorHandler;
import com.sks.gateway.util.UserHelper;
import com.sks.recipes.api.RecipeRequestMessage;
import com.sks.recipes.api.RecipeResponseMessage;
import com.sks.recipes.api.RecipeSender;
import com.sks.recipes.api.dto.CreateRecipeDTO;
import com.sks.recipes.api.dto.RecipeDTO;
import com.sks.users.api.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class RecipesResourceTest {
    @Mock
    private RecipeSender recipeSender;

    @Mock
    private UserHelper userHelper;

    @Mock
    private Principal principal;

    @Mock
    MessageErrorHandler messageErrorHandler;

    private RecipesResource controller;

    @BeforeEach
    public void setUp() {
        recipeSender = mock(RecipeSender.class);
        userHelper = mock(UserHelper.class);
        principal = mock(Principal.class);
        messageErrorHandler = mock(MessageErrorHandler.class);
        controller = new RecipesResource(recipeSender, userHelper, messageErrorHandler);
    }

    @Test
    void getRecipeByIdReturnsRecipeWhenFound() {
        int id = 1;
        RecipeDTO recipe = new RecipeDTO();
        RecipeResponseMessage response = new RecipeResponseMessage();
        response.setRecipes(List.of(recipe));
        when(recipeSender.sendRequest(any())).thenReturn(response);

        RecipeDTO result = controller.getRecipeById(id);

        assertEquals(recipe, result);
    }

    @Test
    void getRecipeByIdThrowsNotFoundWhenRecipeNotFound() {
        int id = 1;
        final RecipeResponseMessage response = new RecipeResponseMessage();
        response.setRecipes(List.of());
        when(recipeSender.sendRequest(any())).thenReturn(response);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> controller.getRecipeById(id));

        assertEquals(exception.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void getRecipeByIdReturns500WhenErrorOnMessageSend() {
        final RecipeResponseMessage responseMessage = new RecipeResponseMessage();
        responseMessage.setDidError(true);
        when(recipeSender.sendRequest(any(RecipeRequestMessage.class))).thenReturn(responseMessage);
        doThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error")).when(messageErrorHandler).handle(responseMessage);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.getRecipeById(1);
        });

        assertEquals("Internal Server Error", exception.getReason());
    }

    @Test
    void getMultipleRecipesByIdReturnsRecipes() {
        long[] ids = {1L, 2L};
        RecipeResponseMessage response = mock(RecipeResponseMessage.class);
        RecipeDTO recipe1 = new RecipeDTO();
        RecipeDTO recipe2 = new RecipeDTO();
        LinkedList<RecipeDTO> recipes = new LinkedList<>();
        recipes.add(recipe1);
        recipes.add(recipe2);
        when(recipeSender.sendRequest(any())).thenReturn(response);
        when(response.getRecipes()).thenReturn(recipes);

        RecipeDTO[] result = controller.getMultipleRecipesById(ids);

        assertEquals(2, result.length);
        assertEquals(recipe1, result[0]);
        assertEquals(recipe2, result[1]);
    }

    @Test
    void getMultipleRecipeReturns500WhenErrorOnMessageSend() {
        final RecipeResponseMessage responseMessage = new RecipeResponseMessage();
        responseMessage.setDidError(true);
        when(recipeSender.sendRequest(any(RecipeRequestMessage.class))).thenReturn(responseMessage);
        doThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error")).when(messageErrorHandler).handle(responseMessage);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.getMultipleRecipesById(new long[]{1, 2});
        });

        assertEquals("Internal Server Error", exception.getReason());
    }

    @Test
    void createRecipeReturnsCreatedRecipe() {
        CreateRecipeDTO createRecipeDTO = new CreateRecipeDTO();
        UserDTO user = new UserDTO();
        user.setUserId(1L);
        RecipeResponseMessage response = mock(RecipeResponseMessage.class);
        RecipeDTO recipe = new RecipeDTO();
        when(userHelper.getCurrentInternalUser(principal)).thenReturn(user);
        when(recipeSender.sendRequest(any(RecipeRequestMessage.class))).thenReturn(response);
        when(response.getRecipe()).thenReturn(recipe);

        RecipeDTO result = controller.createRecipe(createRecipeDTO, principal);

        assertEquals(recipe, result);
    }

    @Test
    void createRecipeThrowsUnauthorizedWhenUserNotAuthenticated() {
        CreateRecipeDTO createRecipeDTO = new CreateRecipeDTO();
        when(userHelper.getCurrentInternalUser(principal)).thenReturn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> controller.createRecipe(createRecipeDTO, principal));

        assertEquals(exception.getStatusCode(), HttpStatus.UNAUTHORIZED);
    }

    @Test
    void createRecipeReturns500WhenErrorOnMessageSend() {
        final RecipeResponseMessage responseMessage = new RecipeResponseMessage();
        responseMessage.setDidError(true);
        UserDTO user = new UserDTO();
        user.setUserId(1L);

        when(recipeSender.sendRequest(any(RecipeRequestMessage.class))).thenReturn(responseMessage);
        when(userHelper.getCurrentInternalUser(principal)).thenReturn(user);
        doThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error")).when(messageErrorHandler).handle(responseMessage);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.createRecipe(new CreateRecipeDTO(), principal);
        });

        assertEquals("Internal Server Error", exception.getReason());
    }

    @Test
    void updateRecipeReturnsUpdatedRecipe() {
        int id = 1;
        CreateRecipeDTO createRecipeDTO = new CreateRecipeDTO();
        createRecipeDTO.setId(id);
        UserDTO user = new UserDTO();
        user.setUserId(1L);
        RecipeResponseMessage response = mock(RecipeResponseMessage.class);
        RecipeDTO recipe = new RecipeDTO();
        when(userHelper.getCurrentInternalUser(principal)).thenReturn(user);
        when(recipeSender.sendRequest(any(RecipeRequestMessage.class))).thenReturn(response);
        when(response.getRecipe()).thenReturn(recipe);

        RecipeDTO result = controller.updateRecipe(id, createRecipeDTO, principal);

        assertEquals(recipe, result);
    }

    @Test
    void updateRecipeThrowsBadRequestWhenIdMismatch() {
        int id = 1;
        CreateRecipeDTO createRecipeDTO = new CreateRecipeDTO();
        createRecipeDTO.setId(2);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> controller.updateRecipe(id, createRecipeDTO, principal));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void updateRecipeReturns500WhenErrorOnMessageSend() {
        final RecipeResponseMessage responseMessage = new RecipeResponseMessage();
        responseMessage.setDidError(true);
        UserDTO user = new UserDTO();
        user.setUserId(1L);
        final CreateRecipeDTO recipe = new CreateRecipeDTO();
        recipe.setId(1);

        when(recipeSender.sendRequest(any(RecipeRequestMessage.class))).thenReturn(responseMessage);
        when(userHelper.getCurrentInternalUser(principal)).thenReturn(user);
        doThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error")).when(messageErrorHandler).handle(responseMessage);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.updateRecipe(1, recipe, principal);
        });

        assertEquals("Internal Server Error", exception.getReason());
    }

    @Test
    void deleteRecipeReturnsNoContentWhenSuccessful() {
        int id = 1;
        RecipeResponseMessage response = mock(RecipeResponseMessage.class);
        when(recipeSender.sendRequest(any())).thenReturn(response);
        when(response.isWasSuccessful()).thenReturn(true);

        ResponseEntity<Void> result = controller.deleteRecipe(id);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    void deleteRecipeThrowsInternalServerErrorWhenFailed() {
        int id = 1;
        RecipeResponseMessage response = mock(RecipeResponseMessage.class);
        when(recipeSender.sendRequest(any())).thenReturn(response);
        when(response.isWasSuccessful()).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> controller.deleteRecipe(id));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    void deleteRecipeReturns500WhenErrorOnMessageSend() {
        final RecipeResponseMessage responseMessage = new RecipeResponseMessage();
        responseMessage.setDidError(true);
        when(recipeSender.sendRequest(any(RecipeRequestMessage.class))).thenReturn(responseMessage);
        doThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error")).when(messageErrorHandler).handle(responseMessage);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.deleteRecipe(1);
        });

        assertEquals("Internal Server Error", exception.getReason());
    }

    @Test
    void getRecipesByUserReturnsRecipesWhenFound() {
        long userId = 1L;
        RecipeDTO recipe = new RecipeDTO();
        RecipeResponseMessage response = new RecipeResponseMessage();
        response.setRecipes(List.of(recipe));
        when(recipeSender.sendRequest(any())).thenReturn(response);

        List<RecipeDTO> result = controller.getRecipesByUser(userId);

        assertEquals(1, result.size());
        assertEquals(recipe, result.getFirst());
    }

    @Test
    void getRecipesByUserReturnsEmptyListWhenNoRecipesFound() {
        long userId = 1L;
        RecipeResponseMessage response = new RecipeResponseMessage();
        response.setRecipes(List.of());
        when(recipeSender.sendRequest(any())).thenReturn(response);

        List<RecipeDTO> result = controller.getRecipesByUser(userId);

        assertEquals(0, result.size());
    }

    @Test
    void getRecipesByUserThrowsInternalServerErrorWhenMessageSendFails() {
        long userId = 1L;
        RecipeResponseMessage response = new RecipeResponseMessage();
        response.setDidError(true);
        when(recipeSender.sendRequest(any())).thenReturn(response);
        doThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error")).when(messageErrorHandler).handle(response);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> controller.getRecipesByUser(userId));

        assertEquals("Internal Server Error", exception.getReason());
    }
}
