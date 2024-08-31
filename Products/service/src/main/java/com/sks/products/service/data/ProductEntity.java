package com.sks.products.service.data;

import jakarta.persistence.*;

/**
 * Entity class representing a product.
 */
@Entity
@Table(name = "products")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_ID")
    private Long id;

    @Column(name = "PRODUCT_NAME")
    private String name;

    @Column(name = "PRODUCT_UNIT")
    private String unit;

    /**
     * Default constructor for ProductEntity.
     */
    public ProductEntity() {
    }

    /**
     * Parameterized constructor for ProductEntity.
     *
     * @param name the name of the product
     * @param unit the unit of the product
     */
    public ProductEntity(String name, String unit) {
        this.name = name;
        this.unit = unit;
    }

    /**
     * Gets the ID of the product.
     *
     * @return the product ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of the product.
     *
     * @param id the product ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the name of the product.
     *
     * @return the product name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the product.
     *
     * @param name the product name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the unit of the product.
     *
     * @return the product unit
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Sets the unit of the product.
     *
     * @param unit the product unit
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }
}