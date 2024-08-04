package com.sks.fridge.service.data.repo;

import com.sks.fridge.service.data.entity.FridgeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FridgeRepository extends JpaRepository<FridgeEntity, Long> {
}
