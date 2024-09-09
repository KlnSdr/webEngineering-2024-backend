package com.sks.products.api;

/**
 * Data Transfer Object (DTO) for Product.
 */
public class ProductDTO {
    private long id;
    private String name;
    private String unit;

    /**
     * Default constructor for ProductDTO.
     */
    public ProductDTO() {
    }

    /**
     * Parameterized constructor for ProductDTO.
     *
     * @param id the product ID
     * @param name the product name
     * @param unit the unit of measurement for the product
     */
    public ProductDTO(long id, String name, String unit) {
        this.id = id;
        this.name = name;
        this.unit = unit;
    }

    /**
     * Gets the product ID.
     *
     * @return the product ID
     */
    public long getId() {
        return this.id;
    }

    /**
     * Gets the product name.
     *
     * @return the product name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the unit of measurement for the product.
     *
     * @return the unit of measurement
     */
    public String getUnit() {
        return this.unit;
    }
}