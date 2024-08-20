package com.sks.recipes.api.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class RecipeDTO {
    private long id;
    private String title;
    private String imgUri;
    private String description;
    private boolean isPrivate;
    private Date creationDate;
    private String ownerUri;
    private List<String> likedByUserUris;
    private List<String> productUris;
    private Map<String, Integer> productQuantities;

    public RecipeDTO() {
    }

    public RecipeDTO(long id, String title, String description, String imgUri,  Date creationDate, String ownerUri) {
        this.id = id;
        this.title = title;
        this.imgUri = imgUri;
        this.description = description;
        this.creationDate = creationDate;
        this.ownerUri = ownerUri;
    }

    public RecipeDTO(long id, String title, String description, String imgUri, boolean isPrivate, Date creationDate, String ownerUri, List<String> likedByUserUris, List<String> productUris, Map<String, Integer> productQuantities) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imgUri = imgUri;
        this.isPrivate = isPrivate;
        this.creationDate = creationDate;
        this.ownerUri = ownerUri;
        this.likedByUserUris = likedByUserUris;
        this.productUris = productUris;
        this.productQuantities = productQuantities;
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

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public List<String> getLikedByUserUris() {
        return likedByUserUris;
    }

    public void setLikedByUserUris(List<String> likedByUserUris) {
        this.likedByUserUris = likedByUserUris;
    }

    public List<String> getProductUris() {
        return productUris;
    }

    public void setProductUris(List<String> productUris) {
        this.productUris = productUris;
    }

    public Map<String, Integer> getProductQuantities() {
        return productQuantities;
    }

    public void setProductQuantities(Map<String, Integer> productQuantities) {
        this.productQuantities = productQuantities;
    }
}
