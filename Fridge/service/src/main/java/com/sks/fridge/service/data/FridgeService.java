package com.sks.fridge.service.data;

import com.sks.fridge.service.data.FridgeEntity;
import com.sks.fridge.service.data.FridgeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FridgeService {
    private FridgeRepository fridgeRepository;

    public List<FridgeEntity> getAll() {
        return fridgeRepository.findAll();
    }

    public FridgeEntity save(FridgeEntity fridgeEntity) {
        return fridgeRepository.save(fridgeEntity);
    }
}
