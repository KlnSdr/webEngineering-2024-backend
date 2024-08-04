package com.sks.recipes.service.data.repo;

import com.sks.recipes.service.data.entity.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<RecipeEntity, Long> {
}
