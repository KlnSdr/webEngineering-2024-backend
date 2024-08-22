package com.sks.recipes.service.data.service;

import com.sks.recipes.api.RecipeRequestMessage;
import com.sks.recipes.api.RecipeSender;
import com.sks.recipes.api.dto.RecipeDTO;
import com.sks.recipes.service.Listener;
import com.sks.recipes.service.data.entity.RecipeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

class ListenerTest {

    @Mock
    private RecipeSender sender;

    @Mock
    private RecipeService service;

    @InjectMocks
    private Listener listener;

    @BeforeEach
    void setUp() {
        service = mock(RecipeService.class);
        sender = mock(RecipeSender.class);
        listener = new Listener(sender, service);
    }

    @Test
    void testListenerByName() {
        final RecipeRequestMessage message = new RecipeRequestMessage("Tomatensoße");
        RecipeEntity entity = new RecipeEntity ( 1L, "Tomatensoße","description", "imageUri", false, new Timestamp(0L), "ownerUri", Collections.singletonList("0"), Collections.singletonList("Tomaten"), Map.of("Tomaten",3));
        when(service.findByName("Tomatensoße")).thenReturn(List.of(entity));

        listener.listen(message);

        List<RecipeDTO> expected = List.of(new RecipeDTO( 1L, "Tomatensoße","description", "imageUri", false, new Timestamp(0L), "ownerUri", Collections.singletonList("0"), Collections.singletonList("Tomaten"), Map.of("Tomaten",3)));
        verify(sender).sendResponse(eq(message), argThat(response -> {
            List<RecipeDTO> payload = response.getRecipes();
            return payload.size() == 1 && equals(expected, payload);
        }));

    }

    @Test
    void testListenerByProduct() {
        final RecipeRequestMessage message = new RecipeRequestMessage(new String[]{"Tomaten","Salz"});
        List<RecipeEntity> entities = List.of(new RecipeEntity(1L, "Tomatensoße", "description", "imageUri", false, new Timestamp(0L), "ownerUri", Collections.singletonList("0"), List.of("Tomaten", "Salz"), Map.of("Tomaten", 3, "Salz", 3)),
                new RecipeEntity(2L, "Special Tomatensoße", "description", "imageUri", false, new Timestamp(0L), "ownerUri", Collections.singletonList("0"), List.of("Tomaten", "Salz"), Map.of("Tomaten", 3, "Salz", 1)));
        when(service.findByProducts(List.of("Tomaten","Salz"))).thenReturn(entities);


        listener.listen(message);

        List<RecipeDTO> expected = List.of(new RecipeDTO(1L, "Tomatensoße", "description", "imageUri", false, new Timestamp(0L), "ownerUri", Collections.singletonList("0"), List.of("Tomaten", "Salz"), Map.of("Tomaten", 3, "Salz", 3))
                , new RecipeDTO(2L, "Special Tomatensoße", "description", "imageUri", false, new Timestamp(0L), "ownerUri", Collections.singletonList("0"), List.of("Tomaten", "Salz"), Map.of("Tomaten", 3, "Salz", 1)));
        verify(sender).sendResponse(eq(message), argThat(response -> {
            List<RecipeDTO> payload = response.getRecipes();
            return payload.size() == 2 && equals(expected, payload);
        }));
    }
    @Test
    void testListenerByProductMoreProducts() {
        final RecipeRequestMessage message = new RecipeRequestMessage(new String[]{"Tomaten","Salz", "Pfeffer"});
        List<RecipeEntity> entities = List.of(new RecipeEntity(1L, "Tomatensoße", "description", "imageUri", false, new Timestamp(0L), "ownerUri", Collections.singletonList("0"), List.of("Tomaten", "Salz"), Map.of("Tomaten", 3, "Salz", 3)),
                new RecipeEntity(2L, "Special Tomatensoße", "description", "imageUri", false, new Timestamp(0L), "ownerUri", Collections.singletonList("0"), List.of("Tomaten", "Salz"), Map.of("Tomaten", 3, "Salz", 1)));
        when(service.findByProducts(List.of("Tomaten","Salz"))).thenReturn(entities);


        listener.listen(message);

        List<RecipeDTO> expected = List.of();
        verify(sender).sendResponse(eq(message), argThat(response -> {
            List<RecipeDTO> payload = response.getRecipes();
            return payload.isEmpty() && equals(expected, payload);
        }));


    }

        private boolean equals(List<RecipeDTO> expected, List<RecipeDTO> payload) {
        if (expected.size() != payload.size()) {
            return false;
        }
        for (int i = 0; i < expected.size(); i++) {
            if (expected.get(i).getId() != payload.get(i).getId()) {
                return false;
            }
            if (!expected.get(i).getTitle().equals(payload.get(i).getTitle())) {
                return false;
            }
            if (!expected.get(i).getDescription().equals(payload.get(i).getDescription())) {
                return false;
            }
            if (!expected.get(i).getImgUri().equals(payload.get(i).getImgUri())) {
                return false;
            }
            if (expected.get(i).isPrivate() != payload.get(i).isPrivate()) {
                return false;
            }
            if (!expected.get(i).getCreationDate().equals(payload.get(i).getCreationDate())) {
                return false;
            }
            if (!expected.get(i).getOwnerUri().equals(payload.get(i).getOwnerUri())) {
                return false;
            }
            if (!expected.get(i).getLikedByUserUris().equals(payload.get(i).getLikedByUserUris())) {
                return false;
            }
            if (!expected.get(i).getProductUris().equals(payload.get(i).getProductUris())) {
                return false;
            }
            if (!expected.get(i).getProductQuantities().equals(payload.get(i).getProductQuantities())) {
                return false;
            }
        }

        return true;

    }

}
