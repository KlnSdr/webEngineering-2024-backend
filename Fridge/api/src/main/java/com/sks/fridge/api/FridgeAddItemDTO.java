package com.sks.fridge.api;

/**
 * Data Transfer Object for adding an item to the fridge.
 */
public class FridgeAddItemDTO {

    private long productID;
    private Integer quantity;

    /**
     * Default constructor.
     */
    public FridgeAddItemDTO() {}

    /**
     * Constructor with parameters.
     *
     * @param productID the ID of the product
     * @param quantity the quantity of the product
     */
    public FridgeAddItemDTO(long productID, Integer quantity) {
        this.productID = productID;
        this.quantity = quantity;
    }

    /**
     * Gets the product ID.
     *
     * @return the product ID
     */
    public long getProductID() {
        return productID;
    }

    /**
     * Sets the product ID.
     *
     * @param productID the new product ID
     */
    public void setProductID(long productID) {
        this.productID = productID;
    }

    /**
     * Gets the quantity.
     *
     * @return the quantity
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity.
     *
     * @param quantity the new quantity
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

}