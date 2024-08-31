package com.sks.products.service.data;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing products.
 */
@Service
public class ProductsService {
    private final ProductsRepository productsRepository;

    /**
     * Constructs a ProductsService with the specified ProductsRepository.
     *
     * @param productsRepository the repository for managing product entities
     */
    public ProductsService(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    /**
     * Retrieves all product entities.
     *
     * @return a list of all product entities
     */
    public List<ProductEntity> getAll() {
        return productsRepository.findAll();
    }

    /**
     * Finds a product entity by its ID.
     *
     * @param productId the ID of the product to find
     * @return an Optional containing the found product entity, or empty if not found
     */
    public Optional<ProductEntity> find(long productId) {
        return productsRepository.findById(productId);
    }

    /**
     * Saves a product entity.
     *
     * @param demoEntity the product entity to save
     * @return the saved product entity
     */
    public ProductEntity save(ProductEntity demoEntity) {
        return productsRepository.save(demoEntity);
    }

    /**
     * Saves a list of product entities.
     *
     * @param demoEntities the list of product entities to save
     * @return the list of saved product entities
     */
    public List<ProductEntity> saveAll(List<ProductEntity> demoEntities) {
        return productsRepository.saveAll(demoEntities);
    }
}