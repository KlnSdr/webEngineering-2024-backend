package com.sks.products.service.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProductsServiceTest {
    @Mock
    private ProductsRepository productsRepository;

    private ProductsService productsService;

    @BeforeEach
    public void setUp() {
        productsRepository = mock(ProductsRepository.class);
        productsService = new ProductsService(productsRepository);
    }

    @Test
    public void testGetAll() {
        ProductEntity product1 = new ProductEntity("", ""); // Assume proper initialization
        product1.setId(1L);
        ProductEntity product2 = new ProductEntity("", ""); // Assume proper initialization
        product1.setId(2L);
        List<ProductEntity> products = Arrays.asList(product1, product2);

        when(productsRepository.findAll()).thenReturn(products);

        List<ProductEntity> result = productsService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(product1.getId(), result.get(0).getId());
        assertEquals(product2.getId(), result.get(1).getId());
    }

    @Test
    public void testFind_Success() {
        ProductEntity product = new ProductEntity("", ""); // Assume proper initialization
        product.setId(1L);
        when(productsRepository.findById(1L)).thenReturn(Optional.of(product));

        Optional<ProductEntity> result = productsService.find(1L);

        assertTrue(result.isPresent());
        assertEquals(product.getId(), result.get().getId());
    }

    @Test
    public void testFind_NotFound() {
        when(productsRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<ProductEntity> result = productsService.find(1L);

        assertFalse(result.isPresent());
    }

    @Test
    public void testSave() {
        ProductEntity product = new ProductEntity("", ""); // Assume proper initialization
        product.setId(1L);
        when(productsRepository.save(product)).thenReturn(product);

        ProductEntity result = productsService.save(product);

        assertNotNull(result);
        assertEquals(product.getId(), result.getId());
    }

    @Test
    public void testSaveAll() {
        ProductEntity product1 = new ProductEntity("", ""); // Assume proper initialization
        product1.setId(1L);
        ProductEntity product2 = new ProductEntity("", ""); // Assume proper initialization
        product2.setId(2L);
        List<ProductEntity> products = Arrays.asList(product1, product2);

        when(productsRepository.saveAll(products)).thenReturn(products);

        List<ProductEntity> result = productsService.saveAll(products);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(product1.getId(), result.get(0).getId());
        assertEquals(product2.getId(), result.get(1).getId());
    }
}
