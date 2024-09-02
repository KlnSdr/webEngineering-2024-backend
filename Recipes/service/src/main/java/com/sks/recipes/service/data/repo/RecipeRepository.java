package com.sks.recipes.service.data.repo;

import com.sks.recipes.service.data.entity.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<RecipeEntity, Long> {
    Optional<RecipeEntity> findById(long id);

    List<RecipeEntity> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description);

    // thanks to dr. faustus for this query
    @Query(value = "SELECT recipe_id FROM recipe_products GROUP BY recipe_id HAVING COUNT(*) = SUM(product_uri IN :productUris)", nativeQuery = true)
    List<Long> findByProductsContaining(@Param("productUris") List<String> productUris);

    void deleteById(long id);
}
