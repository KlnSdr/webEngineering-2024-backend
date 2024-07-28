package com.sks.products.api;

public class ProductDTO {
    private long id;
    private String name;
    private String unit;

    public ProductDTO() {
    }

    public ProductDTO(long id, String name, String unit) {
        this.id = id;
        this.name = name;
        this.unit = unit;
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getUnit() {
        return this.unit;
    }
}
