package com.sks.recipes.service.data.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "recipes")
public class RecipeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RECIPE_ID")
    private Long id;
    @Column(name = "RECIPE_TITLE")
    private String title;
    @Column(name = "RECIPE_DESCRIPTION")
    private String description;
    @Column(name = "RECIPE_IMAGE_URI")
    private String imageUri;
    @Column(name = "RECIPE_IS_PRIVATE")
    private boolean isPrivate;
    @Column(name = "RECIPE_CREATION_DATE", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp creationDate;
    @Column(name = "RECIPE_OWNER_URI")
    private String ownerUri;
    @ElementCollection
    @CollectionTable(name = "recipe_likes", joinColumns = @JoinColumn(name = "RECIPE_ID"))
    @Column(name = "USER_URI")
    private List<String> likedByUserUris;
    @ElementCollection
    @CollectionTable(name = "recipe_products", joinColumns = @JoinColumn(name = "RECIPE_ID"))
    @Column(name = "PRODUCT_URI")
    private List<String> productUris;
    @ElementCollection
    @CollectionTable(name = "recipe_product_quantities", joinColumns = @JoinColumn(name = "RECIPE_ID"))
    @MapKeyColumn(name = "PRODUCT_URI")
    @Column(name = "QUANTITY")
    private Map<String, Integer> productQuantities;

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

    public String getOwnerUri() {
        return ownerUri;
    }

    public void setOwnerUri(String ownerUri) {
        this.ownerUri = ownerUri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}