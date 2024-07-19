package com.sks.gateway.products.rest;

public class ProductResponse {
    private long id;
    private String name;
    private String unit;

    public ProductResponse() {
    }

    public ProductResponse(long id, String name, String unit) {
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
