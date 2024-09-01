package com.sks.gateway.recipes.rest;

import com.sks.recipes.api.RecipeRequestMessage;
import com.sks.recipes.api.RecipeResponseMessage;
import com.sks.recipes.api.RecipeSender;
import com.sks.recipes.api.dto.RecipeDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RecipeSearchRestTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecipeSender recipeSender;

    @BeforeEach
    public void setup() {
        // Setup common mock behavior if needed
    }

    @Test
    public void testGetAllRecipesBySearchString_Success() throws Exception {
        final Date date = new Date();
        RecipeDTO recipe1 = new RecipeDTO(1, "Pancakes", "Mix and cook", "/images/42", date, "/users/id/1");
        RecipeDTO recipe2 = new RecipeDTO(2, "Omelette", "Beat and cook", "/images/42", date, "/users/id/1");
        RecipeResponseMessage responseMessage = new RecipeResponseMessage(Arrays.asList(recipe1, recipe2));
        when(recipeSender.sendRequest(any(RecipeRequestMessage.class))).thenReturn(responseMessage);

        mockMvc.perform(get("/search/recipes")
                        .param("searchString", "cook")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'id':1,'title':'Pancakes','imgUri': '/images/42','description':'Mix and cook','ownerUri':'/users/id/1','likedByUserUris':  null,'productUris':  null, 'productQuantities':  null,'private': false,'creationDate': '" + toIsoString(date) + "'},{'id':2,'title':'Omelette','imgUri': '/images/42','description':'Beat and cook','ownerUri':'/users/id/1','likedByUserUris':  null,'productUris':  null, 'productQuantities':  null,'private': false,'creationDate': '" + toIsoString(date) + "'}]"));
    }

    @Test
    public void testGetAllRecipesBySearchString_NotFound() throws Exception {
        RecipeResponseMessage responseMessage = new RecipeResponseMessage(Collections.emptyList());
        when(recipeSender.sendRequest(any(RecipeRequestMessage.class))).thenReturn(responseMessage);

        mockMvc.perform(get("/search/recipes")
                        .param("searchString", "nonexistent")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testSearchRecipeByProducts_Success() throws Exception {
        final Date date = new Date();
        RecipeDTO recipe1 = new RecipeDTO(1, "Pancakes", "Mix and cook", "/images/42", date, "/users/id/1");
        RecipeDTO recipe2 = new RecipeDTO(2, "Omelette", "Beat and cook", "/images/42", date, "/users/id/1");
        RecipeResponseMessage responseMessage = new RecipeResponseMessage(Arrays.asList(recipe1, recipe2));
        when(recipeSender.sendRequest(any(RecipeRequestMessage.class))).thenReturn(responseMessage);

        mockMvc.perform(post("/search/recipes/by-products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"eggs\", \"milk\"]")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'id':1,'title':'Pancakes','imgUri': '/images/42','description':'Mix and cook','ownerUri':'/users/id/1','likedByUserUris':  null,'productUris':  null, 'productQuantities':  null,'private': false,'creationDate': '" + toIsoString(date) + "'},{'id':2,'title':'Omelette','imgUri': '/images/42','description':'Beat and cook','ownerUri':'/users/id/1','likedByUserUris':  null,'productUris':  null, 'productQuantities':  null,'private': false,'creationDate': '" + toIsoString(date) + "'}]"));
    }

    @Test
    public void testSearchRecipeByProducts_NotFound() throws Exception {
        RecipeResponseMessage responseMessage = new RecipeResponseMessage(Collections.emptyList());
        when(recipeSender.sendRequest(any(RecipeRequestMessage.class))).thenReturn(responseMessage);

        mockMvc.perform(post("/search/recipes/by-products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"nonexistent\"]")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private String toIsoString(Date date) {
        ZonedDateTime zonedDateTime = date.toInstant().atZone(ZoneId.of("GMT"));
        return DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSxxx").format(zonedDateTime);
    }
}
