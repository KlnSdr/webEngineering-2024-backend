package com.sks.fridge.service.data.service;

import com.sks.fridge.service.data.entity.FridgeEntity;
import com.sks.fridge.service.data.repo.FridgeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FridgeService {
    private final FridgeRepository fridgeRepository;

    public FridgeService(FridgeRepository fridgeRepository) {
        this.fridgeRepository = fridgeRepository;
    }

    public List<FridgeEntity> getAll() {
        return fridgeRepository.findAll();
    }

    public FridgeEntity save(FridgeEntity fridgeEntity) {
        return fridgeRepository.save(fridgeEntity);
    }

    public Optional<FridgeEntity> findByUserUri(String uri) {
        return fridgeRepository.findByUserUriEquals(uri);
    }
}
