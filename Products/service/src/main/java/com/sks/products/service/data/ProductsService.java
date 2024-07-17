package com.sks.products.service.data;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductsService {
    private final ProductsRepository productsRepository;

    public ProductsService(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    public List<ProductEntity> getAll() {
        return productsRepository.findAll();
    }

    public ProductEntity save(ProductEntity demoEntity) {
        return productsRepository.save(demoEntity);
    }

    public List<ProductEntity> saveAll(List<ProductEntity> demoEntities) {
        return productsRepository.saveAll(demoEntities);
    }
}
