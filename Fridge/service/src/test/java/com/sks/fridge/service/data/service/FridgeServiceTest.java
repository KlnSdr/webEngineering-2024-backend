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

import static org.junit.jupiter.api.Assertions.assertEquals;
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
}
