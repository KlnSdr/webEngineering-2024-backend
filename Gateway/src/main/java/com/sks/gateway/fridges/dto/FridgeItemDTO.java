package com.sks.gateway.fridges.dto;

public class FridgeItemDTO {

        private String name;
        private long id;
        private String unit;
        private double quantity;

        public FridgeItemDTO(){}

        public FridgeItemDTO(String name, long id, String unit, double quantity) {
            this.name = name;
            this.id = id;
            this.unit = unit;
            this.quantity = quantity;
        }

        // Getters and Setters
        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public long getId() {
                return id;
        }

        public void setId(long id) {
                this.id = id;
        }

        public String getUnit() {
                return unit;
        }

        public void setUnit(String unit) {
                this.unit = unit;
        }

        public double getQuantity() {
                return quantity;
        }

        public void setQuantity(double quantity) {
                this.quantity = quantity;
        }

}
