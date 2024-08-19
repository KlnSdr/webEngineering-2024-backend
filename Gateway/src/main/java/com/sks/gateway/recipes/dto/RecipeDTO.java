package com.sks.gateway.recipes.dto;

import java.util.Date;

public class RecipeDTO {
    private long id;
    private String title;
    private String imgUri;
    private String description;
    private Date creationDate;
    private String ownerUri;

    public RecipeDTO() {
    }

    public RecipeDTO(long id, String title, String imgUri, String description, Date creationDate, String ownerUri) {
        this.id = id;
        this.title = title;
        this.imgUri = imgUri;
        this.description = description;
        this.creationDate = creationDate;
        this.ownerUri = ownerUri;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getOwnerUri() {
        return ownerUri;
    }

    public void setOwnerUri(String ownerUri) {
        this.ownerUri = ownerUri;
    }
}
