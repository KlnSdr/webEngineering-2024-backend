package com.sks.recipes.api.dto;

import java.util.Map;

public class CreateRecipeDTO {
    private String title;
    private String imgUri;
    private String description;
    private String ownerUri;
    private boolean isPrivate;
    private Map<String, Integer> productQuantities;

    public CreateRecipeDTO() {
    }

    public CreateRecipeDTO(String title, String description, String imgUri, String ownerUri) {
        this.title = title;
        this.description = description;
        this.imgUri = imgUri;
        this.ownerUri = ownerUri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwnerUri() {
        return ownerUri;
    }

    public void setOwnerUri(String ownerUri) {
        this.ownerUri = ownerUri;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public Map<String, Integer> getProductQuantities() {
        return productQuantities;
    }

    public void setProductQuantities(Map<String, Integer> productQuantities) {
        this.productQuantities = productQuantities;
    }
}
