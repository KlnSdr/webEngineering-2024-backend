package com.sks.gateway.fridges.dto;

/**
 * Data Transfer Object (DTO) representing an item in a fridge.
 */
public class FridgeItemDTO {

    /**
     * The name of the fridge item.
     */
    private String name;

    /**
     * The unique identifier of the fridge item.
     */
    private long id;

    /**
     * The unit of measurement for the fridge item.
     */
    private String unit;

    /**
     * The quantity of the fridge item.
     */
    private double quantity;

    /**
     * Default constructor.
     */
    public FridgeItemDTO() {}

    /**
     * Constructs a FridgeItemDTO with the specified name, id, unit, and quantity.
     *
     * @param name the name of the fridge item
     * @param id the unique identifier of the fridge item
     * @param unit the unit of measurement for the fridge item
     * @param quantity the quantity of the fridge item
     */
    public FridgeItemDTO(String name, long id, String unit, double quantity) {
        this.name = name;
        this.id = id;
        this.unit = unit;
        this.quantity = quantity;
    }

    /**
     * Gets the name of the fridge item.
     *
     * @return the name of the fridge item
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the fridge item.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the unique identifier of the fridge item.
     *
     * @return the unique identifier of the fridge item
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the fridge item.
     *
     * @param id the unique identifier to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets the unit of measurement for the fridge item.
     *
     * @return the unit of measurement for the fridge item
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Sets the unit of measurement for the fridge item.
     *
     * @param unit the unit to set
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * Gets the quantity of the fridge item.
     *
     * @return the quantity of the fridge item
     */
    public double getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of the fridge item.
     *
     * @param quantity the quantity to set
     */
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
}