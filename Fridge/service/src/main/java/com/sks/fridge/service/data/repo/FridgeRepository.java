package com.sks.fridge.service.data.repo;

import com.sks.fridge.service.data.entity.FridgeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FridgeRepository extends JpaRepository<FridgeEntity, Long> {
    Optional<FridgeEntity> findByUserUriEquals(String uri);
}
