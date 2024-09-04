package com.sks.gateway.recipes.rest;

import com.sks.gateway.util.UserHelper;
import com.sks.recipes.api.RecipeRequestMessage;
import com.sks.recipes.api.RecipeResponseMessage;
import com.sks.recipes.api.RecipeSender;
import com.sks.recipes.api.dto.RecipeDTO;
import com.sks.users.api.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RecipesResourceRestTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecipeSender recipeSender;

    @MockBean
    private UserHelper userHelper;

    @BeforeEach
    public void setup() {
        // Setup common mock behavior if needed
    }

    @Test
    @WithMockUser(username = "user")
    public void testGetRecipeById_Success() throws Exception {
        final Date date = new Date();
        RecipeDTO recipe = new RecipeDTO(1, "Pancakes", "Mix and cook", "/images/42", date, "/users/id/1");
        RecipeResponseMessage responseMessage = new RecipeResponseMessage(Collections.singletonList(recipe));
        when(recipeSender.sendRequest(any(RecipeRequestMessage.class))).thenReturn(responseMessage);

        mockMvc.perform(get("/recipes/1").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().json("{'id':1,'title':'Pancakes','imgUri': '/images/42','description':'Mix and cook','ownerUri':'/users/id/1','likedByUserUris':  null,'productUris':  null, 'productQuantities':  null,'private': false,'creationDate': '" + toIsoString(date) + "'}"));
    }

    @Test
    public void testGetRecipeById_NotFound() throws Exception {
        RecipeResponseMessage responseMessage = new RecipeResponseMessage(Collections.emptyList());
        when(recipeSender.sendRequest(any(RecipeRequestMessage.class))).thenReturn(responseMessage);

        mockMvc.perform(get("/recipes/1").accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user")
    public void testGetMultipleRecipesById_Success() throws Exception {
        final Date date = new Date();
        RecipeDTO recipe1 = new RecipeDTO(1, "Pancakes", "Mix and cook", "/images/42", date, "/users/id/1");
        RecipeDTO recipe2 = new RecipeDTO(2, "Omelette", "Beat and cook", "/images/42", date, "/users/id/1");
        RecipeResponseMessage responseMessage = new RecipeResponseMessage(List.of(recipe1, recipe2));
        when(recipeSender.sendRequest(any(RecipeRequestMessage.class))).thenReturn(responseMessage);

        mockMvc.perform(post("/recipes/get-multiple").contentType(MediaType.APPLICATION_JSON).content("[1, 2]").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().json("[{'id':1,'title':'Pancakes','imgUri': '/images/42','description':'Mix and cook','ownerUri':'/users/id/1','likedByUserUris':  null,'productUris':  null, 'productQuantities':  null,'private': false,'creationDate': '" + toIsoString(date) + "'},{'id':2,'title':'Omelette','imgUri': '/images/42','description':'Beat and cook','ownerUri':'/users/id/1','likedByUserUris':  null,'productUris':  null, 'productQuantities':  null,'private': false,'creationDate': '" + toIsoString(date) + "'}]"));
    }

    @Test
    @WithMockUser(username = "user")
    public void testCreateRecipe_Success() throws Exception {
        final Date date = new Date();
        UserDTO user = new UserDTO();
        user.setUserId(1L);
        user.setUserName("user");
        RecipeDTO recipe = new RecipeDTO(1, "Pancakes", "Mix and cook", "/images/42", date, "/users/id/1");
        RecipeResponseMessage responseMessage = new RecipeResponseMessage();
        responseMessage.setRecipe(recipe);

        when(userHelper.getCurrentInternalUser(any(Principal.class))).thenReturn(user);
        when(recipeSender.sendRequest(any(RecipeRequestMessage.class))).thenReturn(responseMessage);

        mockMvc.perform(post("/recipes").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Pancakes\",\"instructions\":\"Mix and cook\"}").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().json("{'id':1,'title':'Pancakes','imgUri': '/images/42','description':'Mix and cook','ownerUri':'/users/id/1','likedByUserUris':  null,'productUris':  null, 'productQuantities':  null,'private': false,'creationDate': '" + toIsoString(date) + "'}"));
    }

    @Test
    public void testCreateRecipe_Unauthorized() throws Exception {
        when(userHelper.getCurrentInternalUser(any(Principal.class))).thenReturn(null);

        mockMvc.perform(post("/recipes").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Pancakes\",\"instructions\":\"Mix and cook\"}").accept(MediaType.APPLICATION_JSON)).andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "user")
    public void testUpdateRecipe_Success() throws Exception {
        final Date date = new Date();
        UserDTO user = new UserDTO();
        user.setUserId(1L);
        user.setUserName("user");
        RecipeDTO recipe = new RecipeDTO(1, "Updated Pancakes", "Mix and cook well", "/images/42", date, "/users/id/1");
        RecipeResponseMessage responseMessage = new RecipeResponseMessage();
        responseMessage.setRecipe(recipe);

        when(userHelper.getCurrentInternalUser(any(Principal.class))).thenReturn(user);
        when(recipeSender.sendRequest(any(RecipeRequestMessage.class))).thenReturn(responseMessage);

        mockMvc.perform(put("/recipes/1").contentType(MediaType.APPLICATION_JSON).content("{\"id\":1,\"name\":\"Updated Pancakes\",\"instructions\":\"Mix and cook well\"}").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().json("{'id':1,'title':'Updated Pancakes','imgUri': '/images/42','description':'Mix and cook well','ownerUri':'/users/id/1','likedByUserUris':  null,'productUris':  null, 'productQuantities':  null,'private': false,'creationDate': '" + toIsoString(date) + "'}"));
    }

    @Test
    @WithMockUser(username = "user")
    public void testUpdateRecipe_IdMismatch() throws Exception {
        mockMvc.perform(put("/recipes/1").contentType(MediaType.APPLICATION_JSON).content("{\"id\":2,\"name\":\"Updated Pancakes\",\"instructions\":\"Mix and cook well\"}").accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateRecipe_Unauthorized() throws Exception {
        when(userHelper.getCurrentInternalUser(any(Principal.class))).thenReturn(null);

        mockMvc.perform(put("/recipes/1").contentType(MediaType.APPLICATION_JSON).content("{\"id\":1,\"name\":\"Updated Pancakes\",\"instructions\":\"Mix and cook well\"}").accept(MediaType.APPLICATION_JSON)).andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "user")
    public void testDeleteRecipe_Success() throws Exception {
        RecipeResponseMessage responseMessage = new RecipeResponseMessage();
        responseMessage.setWasSuccessful(true);

        when(recipeSender.sendRequest(any(RecipeRequestMessage.class))).thenReturn(responseMessage);

        mockMvc.perform(delete("/recipes/1").accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "user")
    public void testDeleteRecipe_Failure() throws Exception {
        RecipeResponseMessage responseMessage = new RecipeResponseMessage();
        responseMessage.setWasSuccessful(false);

        when(recipeSender.sendRequest(any(RecipeRequestMessage.class))).thenReturn(responseMessage);

        mockMvc.perform(delete("/recipes/1").accept(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError());
    }

    @Test
    public void testGetRecipeByOwner_Success() throws Exception {
        final Date date = new Date();
        RecipeDTO recipe = new RecipeDTO(1, "Pancakes", "Mix and cook", "/images/42", date, "/users/id/1");
        RecipeResponseMessage responseMessage = new RecipeResponseMessage(Collections.singletonList(recipe));
        when(recipeSender.sendRequest(any(RecipeRequestMessage.class))).thenReturn(responseMessage);

        mockMvc.perform(get("/recipes/user/1").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().json("[{'id':1,'title':'Pancakes','imgUri': '/images/42','description':'Mix and cook','ownerUri':'/users/id/1','likedByUserUris':  null,'productUris':  null, 'productQuantities':  null,'private': false,'creationDate': '" + toIsoString(date) + "'}]"));
    }

    @Test
    public void testGetRecipeByOwner_NothingFound() throws Exception {
        RecipeResponseMessage responseMessage = new RecipeResponseMessage(Collections.emptyList());
        when(recipeSender.sendRequest(any(RecipeRequestMessage.class))).thenReturn(responseMessage);

        mockMvc.perform(get("/recipes/user/1").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().json("[]"));
    }

    private String toIsoString(Date date) {
        ZonedDateTime zonedDateTime = date.toInstant().atZone(ZoneId.of("GMT"));
        return DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSxxx").format(zonedDateTime);
    }
}
