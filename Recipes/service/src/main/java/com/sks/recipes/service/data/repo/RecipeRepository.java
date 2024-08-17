package com.sks.recipes.service.data.repo;

import com.sks.recipes.service.data.entity.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface RecipeRepository extends JpaRepository<RecipeEntity, Long> {

    List<RecipeEntity> findByTitleContainingIgnoreCase(String searchString);

    List<RecipeEntity> findByProductUrisIn(Collection<List<String>> productUris);
}
