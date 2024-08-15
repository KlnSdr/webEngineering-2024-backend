package com.sks.gateway.fridges.dto;

public class FridgeItemDTO {

    public class FridgeItemDTO {
        private String name;
        private String id;
        private String unit;
        private double quantity;

        public FridgeItemDTO(String name, String id, String unit, double quantity) {
            this.name = name;
            this.id = id;
            this.unit = unit;
            this.quantity = quantity;
        }

        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getUnit() { return unit; }
        public void setUnit(String unit) { this.unit = unit; }

        public double getQuantity() { return quantity; }
        public void setQuantity(double quantity) { this.quantity = quantity; }
    }

}
