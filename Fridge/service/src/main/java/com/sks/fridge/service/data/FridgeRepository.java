package com.sks.fridge.service.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FridgeRepository extends JpaRepository<FridgeEntity, Long> {
}
