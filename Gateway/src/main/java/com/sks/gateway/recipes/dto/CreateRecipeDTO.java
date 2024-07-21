package com.sks.gateway.recipes.dto;

public class CreateRecipeDTO {
    private String title;
    private String imgUri;
    private String description;
    private String ownerUri;

    public CreateRecipeDTO() {
    }

    public CreateRecipeDTO(String title, String imgUri, String description, String ownerUri) {
        this.title = title;
        this.imgUri = imgUri;
        this.description = description;
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
}
