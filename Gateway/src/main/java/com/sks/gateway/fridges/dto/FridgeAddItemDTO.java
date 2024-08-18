package com.sks.gateway.fridges.dto;

public class FridgeAddItemDTO {

    private long productID;
    private double quantity;

    public FridgeAddItemDTO() {}

    public FridgeAddItemDTO(long productID, double quantity) {
        this.productID = productID;
        this.quantity = quantity;
    }

    public long getID() {
        return productID;
    }

    public void setID(long productID) {
        this.productID = productID;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

}
