package com.sks.products.service.data;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductsService {
    private ProductsRepository demoRepository;

    public List<ProductEntity> getAll() {
        return demoRepository.findAll();
    }

    public ProductEntity save(ProductEntity demoEntity) {
        return demoRepository.save(demoEntity);
    }
}
