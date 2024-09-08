package com.sks.gateway.recipes.rest;

import com.sks.gateway.common.MessageErrorHandler;
import com.sks.gateway.util.UserHelper;
import com.sks.recipes.api.RecipeRequestMessage;
import com.sks.recipes.api.RecipeResponseMessage;
import com.sks.recipes.api.RecipeSender;
import com.sks.recipes.api.dto.RecipeDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RecipeSearchTest {

    @Mock
    private RecipeSender sender;
    @Mock
    private MessageErrorHandler errorHandler;
    @Mock
    private UserHelper userHelper;

    private SearchResource controller;

    @BeforeEach
    public void setUp() {
        sender = mock(RecipeSender.class);
        errorHandler = mock(MessageErrorHandler.class);
        userHelper = mock(UserHelper.class);
        controller = new SearchResource(sender, errorHandler, userHelper);
    }

    @Test
    public void testSearchRecipeByString() {
        List<RecipeDTO> recipe = List.of(new RecipeDTO(1L, "Tomatensoße", "description", "imageUri", false, new Timestamp(0L), "ownerUri", Collections.singletonList("0"), Collections.singletonList("Tomaten"), Map.of("Tomaten", 3)));
        RecipeResponseMessage responseMessage = new RecipeResponseMessage(recipe);
        when(sender.sendRequest(any(RecipeRequestMessage.class))).thenReturn(responseMessage);
        when(userHelper.getCurrentInternalUser(any())).thenReturn(null);

        List<RecipeDTO> result = controller.getAllRecipesBySearchString("Tomatensoße", null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(recipe.get(0).getId(), result.get(0).getId());
        assertEquals(recipe.get(0).getTitle(), result.get(0).getTitle());
        assertEquals(recipe.get(0).getDescription(), result.get(0).getDescription());
        assertEquals(recipe.get(0).getImgUri(), result.get(0).getImgUri());
        assertEquals(recipe.get(0).isPrivate(), result.get(0).isPrivate());
        assertEquals(recipe.get(0).getCreationDate(), result.get(0).getCreationDate());
        assertEquals(recipe.get(0).getOwnerUri(), result.get(0).getOwnerUri());
        assertEquals(recipe.get(0).getLikedByUserUris(), result.get(0).getLikedByUserUris());
        assertEquals(recipe.get(0).getProductUris(), result.get(0).getProductUris());
        assertEquals(recipe.get(0).getProductQuantities(), result.get(0).getProductQuantities());


    }

    @Test
    public void testSearchRecipesByString_Throws500OnMessageError() {
        RecipeResponseMessage responseMessage = new RecipeResponseMessage();
        responseMessage.setDidError(true);
        when(sender.sendRequest(any(RecipeRequestMessage.class))).thenReturn(responseMessage);
        doThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error")).when(errorHandler).handle(responseMessage);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.getAllRecipesBySearchString("Tomatensoße", null);
        });

        assertEquals("Internal Server Error", exception.getReason());
    }

    @Test
    public void testSearchRecipeByProducts() {
        List<RecipeDTO> recipe = List.of(new RecipeDTO(1L, "Tomatensoße", "description", "imageUri", false, new Timestamp(0L), "ownerUri", Collections.singletonList("0"), Collections.singletonList("Tomaten"), Map.of("Tomaten", 3)));
        RecipeResponseMessage responseMessage = new RecipeResponseMessage(recipe);
        when(sender.sendRequest(any(RecipeRequestMessage.class))).thenReturn(responseMessage);
        when(userHelper.getCurrentInternalUser(any())).thenReturn(null);

        List<RecipeDTO> result = controller.searchRecipeByProducts(new String[]{"Tomaten"}, null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(recipe.get(0).getId(), result.get(0).getId());
        assertEquals(recipe.get(0).getTitle(), result.get(0).getTitle());
        assertEquals(recipe.get(0).getDescription(), result.get(0).getDescription());
        assertEquals(recipe.get(0).getImgUri(), result.get(0).getImgUri());
        assertEquals(recipe.get(0).isPrivate(), result.get(0).isPrivate());
        assertEquals(recipe.get(0).getCreationDate(), result.get(0).getCreationDate());
        assertEquals(recipe.get(0).getOwnerUri(), result.get(0).getOwnerUri());
        assertEquals(recipe.get(0).getLikedByUserUris(), result.get(0).getLikedByUserUris());
        assertEquals(recipe.get(0).getProductUris(), result.get(0).getProductUris());
        assertEquals(recipe.get(0).getProductQuantities(), result.get(0).getProductQuantities());
    }

    @Test
    public void testSearchRecipesByProducts_Throws500OnMessageError() {
        RecipeResponseMessage responseMessage = new RecipeResponseMessage();
        responseMessage.setDidError(true);
        when(sender.sendRequest(any(RecipeRequestMessage.class))).thenReturn(responseMessage);
        doThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error")).when(errorHandler).handle(responseMessage);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.searchRecipeByProducts(new String[]{"Tomaten"}, null);
        });

        assertEquals("Internal Server Error", exception.getReason());
    }
}
