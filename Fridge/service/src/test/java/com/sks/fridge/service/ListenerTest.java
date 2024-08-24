package com.sks.fridge.service;

import com.sks.fridge.api.*;
import com.sks.fridge.service.data.entity.FridgeEntity;
import com.sks.fridge.service.data.service.FridgeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class ListenerTest {

    @Mock
    private FridgeSender sender;

    @Mock
    private FridgeService service;

    private Listener listener;

    @BeforeEach
    void setUp() {
        sender = mock(FridgeSender.class);
        service = mock(FridgeService.class);
        listener = new Listener(sender, service);
    }

    @Test
    void listenHandlesGetRequestSuccessfully() {
        FridgeRequestMessage request = new FridgeRequestMessage();
        request.setRequestType(FridgeRequestType.GET);
        request.setUserId(1L);
        FridgeEntity fridgeEntity = new FridgeEntity();
        fridgeEntity.setUserUri("/users/id/1");
        fridgeEntity.setProductQuantityMap(new HashMap<>());
        when(service.findByUserUri("/users/id/1")).thenReturn(Optional.of(fridgeEntity));

        listener.listen(request);

        verify(sender).sendResponse(eq(request), any(FridgeResponseMessage.class));
    }

    @Test
    void listenHandlesGetRequestWithEmptyFridge() {
        FridgeRequestMessage request = new FridgeRequestMessage();
        request.setRequestType(FridgeRequestType.GET);
        request.setUserId(1L);
        when(service.findByUserUri("/users/id/1")).thenReturn(Optional.empty());

        listener.listen(request);

        verify(sender).sendResponse(eq(request), any(FridgeResponseMessage.class));
    }

    @Test
    void listenHandlesUpdateRequestSuccessfully() {
        FridgeRequestMessage request = new FridgeRequestMessage();
        request.setRequestType(FridgeRequestType.UPDATE);
        request.setUserId(1L);
        FridgeAddItemDTO item = new FridgeAddItemDTO(1L, 2);
        request.setProducts(List.of(item));
        FridgeEntity fridgeEntity = new FridgeEntity();
        fridgeEntity.setUserUri("/users/id/1");
        fridgeEntity.setProductQuantityMap(new HashMap<>());
        when(service.findByUserUri("/users/id/1")).thenReturn(Optional.of(fridgeEntity));
        when(service.save(any(FridgeEntity.class))).thenReturn(fridgeEntity);

        listener.listen(request);

        verify(sender).sendResponse(eq(request), any(FridgeResponseMessage.class));
    }

    @Test
    void listenHandlesUpdateRequestWithNewFridge() {
        FridgeRequestMessage request = new FridgeRequestMessage();
        request.setRequestType(FridgeRequestType.UPDATE);
        request.setUserId(1L);
        FridgeAddItemDTO item = new FridgeAddItemDTO(1L, 2);
        request.setProducts(List.of(item));
        when(service.findByUserUri("/users/id/1")).thenReturn(Optional.empty());
        FridgeEntity fridgeEntity = new FridgeEntity();
        fridgeEntity.setUserUri("/users/id/1");
        fridgeEntity.setProductQuantityMap(new HashMap<>());
        when(service.save(any(FridgeEntity.class))).thenReturn(fridgeEntity);

        listener.listen(request);

        verify(sender).sendResponse(eq(request), any(FridgeResponseMessage.class));
    }

    @Test
    void listenHandlesDeleteRequestSuccessfully() {
        FridgeRequestMessage request = new FridgeRequestMessage();
        request.setRequestType(FridgeRequestType.DELETE);
        request.setUserId(1L);
        request.setProductId(1L);
        FridgeEntity fridgeEntity = new FridgeEntity();
        fridgeEntity.setUserUri("/users/id/1");
        final Map<String, Integer> productQuantityMap = new HashMap<>();
        productQuantityMap.put("/products/1", 2);
        fridgeEntity.setProductQuantityMap(productQuantityMap);
        when(service.findByUserUri("/users/id/1")).thenReturn(Optional.of(fridgeEntity));
        when(service.save(any(FridgeEntity.class))).thenReturn(fridgeEntity);

        listener.listen(request);

        verify(sender).sendResponse(eq(request), any(FridgeResponseMessage.class));
    }

    @Test
    void listenHandlesDeleteRequestWithNonExistentFridge() {
        FridgeRequestMessage request = new FridgeRequestMessage();
        request.setRequestType(FridgeRequestType.DELETE);
        request.setUserId(1L);
        request.setProductId(1L);
        when(service.findByUserUri("/users/id/1")).thenReturn(Optional.empty());

        listener.listen(request);

        verify(sender).sendResponse(eq(request), any(FridgeResponseMessage.class));
    }

}
