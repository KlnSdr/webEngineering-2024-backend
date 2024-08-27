package com.sks.recipes.service.data.repo;

import com.sks.recipes.service.data.entity.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<RecipeEntity, Long> {
    Optional<RecipeEntity> findById(long id);

    List<RecipeEntity> findByTitleContainingIgnoreCase(String searchString);

    List<RecipeEntity> findByProductUrisIn(Collection<String> productUris);

    long deleteById(long id);
}
