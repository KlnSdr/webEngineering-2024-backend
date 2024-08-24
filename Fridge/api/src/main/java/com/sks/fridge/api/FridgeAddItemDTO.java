package com.sks.fridge.api;

public class FridgeAddItemDTO {

    private long productID;
    private Integer quantity;

    public FridgeAddItemDTO() {}

    public FridgeAddItemDTO(long productID, Integer quantity) {
        this.productID = productID;
        this.quantity = quantity;
    }

    public long getID() {
        return productID;
    }

    public void setID(long productID) {
        this.productID = productID;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

}
