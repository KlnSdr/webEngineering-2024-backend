package com.sks.fridge.service.data.service;

import com.sks.fridge.service.data.entity.FridgeEntity;
import com.sks.fridge.service.data.repo.FridgeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


public class FridgeServiceTest {


    @Mock
    private FridgeRepository fridgeRepository;

    @InjectMocks
    private FridgeService fridgeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllReturnsListOfFridges() {
        FridgeEntity fridge = new FridgeEntity();
        when(fridgeRepository.findAll()).thenReturn(List.of(fridge));

        List<FridgeEntity> result = fridgeService.getAll();

        assertEquals(1, result.size());
        assertEquals(fridge, result.getFirst());
    }

    @Test
    void getAllReturnsEmptyListWhenNoFridges() {
        when(fridgeRepository.findAll()).thenReturn(Collections.emptyList());

        List<FridgeEntity> result = fridgeService.getAll();

        assertEquals(0, result.size());
    }

    @Test
    void saveReturnsSavedFridge() {
        FridgeEntity fridge = new FridgeEntity();
        when(fridgeRepository.save(fridge)).thenReturn(fridge);

        FridgeEntity result = fridgeService.save(fridge);

        assertEquals(fridge, result);
    }

    @Test
    void findByUserUriReturnsFridgeWhenUriExists() {
        String uri = "userUri";
        FridgeEntity fridge = new FridgeEntity();
        when(fridgeRepository.findByUserUriEquals(uri)).thenReturn(Optional.of(fridge));

        Optional<FridgeEntity> result = fridgeService.findByUserUri(uri);

        assertTrue(result.isPresent());
        assertEquals(fridge, result.get());
    }

    @Test
    void findByUserUriReturnsEmptyWhenUriDoesNotExist() {
        String uri = "nonExistentUri";
        when(fridgeRepository.findByUserUriEquals(uri)).thenReturn(Optional.empty());

        Optional<FridgeEntity> result = fridgeService.findByUserUri(uri);

        assertFalse(result.isPresent());
    }

}
