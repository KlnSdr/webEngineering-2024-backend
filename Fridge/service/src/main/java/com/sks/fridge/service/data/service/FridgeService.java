package com.sks.fridge.service.data.service;

import com.sks.fridge.service.data.entity.FridgeEntity;
import com.sks.fridge.service.data.repo.FridgeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing fridge-related operations.
 */
@Service
public class FridgeService {
    private final FridgeRepository fridgeRepository;

    /**
     * Constructor for FridgeService.
     *
     * @param fridgeRepository the repository for fridge entities
     */
    public FridgeService(FridgeRepository fridgeRepository) {
        this.fridgeRepository = fridgeRepository;
    }

    /**
     * Retrieves all fridge entities.
     *
     * @return a list of all fridge entities
     */
    public List<FridgeEntity> getAll() {
        return fridgeRepository.findAll();
    }

    /**
     * Saves a fridge entity.
     *
     * @param fridgeEntity the fridge entity to save
     * @return the saved fridge entity
     */
    public FridgeEntity save(FridgeEntity fridgeEntity) {
        return fridgeRepository.save(fridgeEntity);
    }

    /**
     * Finds a fridge entity by user URI.
     *
     * @param uri the user URI
     * @return an Optional containing the found fridge entity, or empty if not found
     */
    public Optional<FridgeEntity> findByUserUri(String uri) {
        return fridgeRepository.findByUserUriEquals(uri);
    }
}