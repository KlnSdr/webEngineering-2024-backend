package com.sks.fridge.service;

import com.sks.fridge.api.*;
import com.sks.fridge.service.data.entity.FridgeEntity;
import com.sks.fridge.service.data.service.FridgeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

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

    @Mock
    private Jackson2JsonMessageConverter converter;

    private Listener listener;

    @BeforeEach
    void setUp() {
        sender = mock(FridgeSender.class);
        service = mock(FridgeService.class);
        converter = mock(Jackson2JsonMessageConverter.class);
        listener = new Listener(sender, service, converter);
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
        when(converter.fromMessage(any())).thenReturn(request);

        listener.listen(null);

        verify(sender).sendResponse(eq(request), any(FridgeResponseMessage.class));
    }

    @Test
    void listenHandlesGetRequestWithEmptyFridge() {
        FridgeRequestMessage request = new FridgeRequestMessage();
        request.setRequestType(FridgeRequestType.GET);
        request.setUserId(1L);
        when(service.findByUserUri("/users/id/1")).thenReturn(Optional.empty());
        when(converter.fromMessage(any())).thenReturn(request);

        listener.listen(null);

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
        when(converter.fromMessage(any())).thenReturn(request);

        listener.listen(null);

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
        when(converter.fromMessage(any())).thenReturn(request);

        listener.listen(null);

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
        when(converter.fromMessage(any())).thenReturn(request);

        listener.listen(null);

        verify(sender).sendResponse(eq(request), any(FridgeResponseMessage.class));
    }

    @Test
    void listenHandlesDeleteRequestWithNonExistentFridge() {
        FridgeRequestMessage request = new FridgeRequestMessage();
        request.setRequestType(FridgeRequestType.DELETE);
        request.setUserId(1L);
        request.setProductId(1L);
        when(service.findByUserUri("/users/id/1")).thenReturn(Optional.empty());
        when(converter.fromMessage(any())).thenReturn(request);

        listener.listen(null);

        verify(sender).sendResponse(eq(request), any(FridgeResponseMessage.class));
    }

    @Test
    public void listenHandlesNonFridgeRequestByDoingNothing() {
        Object nonFridgeRequest = new Object();
        when(converter.fromMessage(any())).thenReturn(nonFridgeRequest);

        listener.listen(null);

        verify(sender, never()).sendResponse(any(), any());
    }

}
