package com.sks.recipes.api.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Data Transfer Object (DTO) for representing a recipe.
 */
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

    /**
     * Default constructor for RecipeDTO.
     */
    public RecipeDTO() {
    }

    /**
     * Parameterized constructor for RecipeDTO.
     *
     * @param id the ID of the recipe
     * @param title the title of the recipe
     * @param description the description of the recipe
     * @param imgUri the image URI of the recipe
     * @param creationDate the creation date of the recipe
     * @param ownerUri the owner URI of the recipe
     */
    public RecipeDTO(long id, String title, String description, String imgUri, Date creationDate, String ownerUri) {
        this.id = id;
        this.title = title;
        this.imgUri = imgUri;
        this.description = description;
        this.creationDate = creationDate;
        this.ownerUri = ownerUri;
    }

    /**
     * Parameterized constructor for RecipeDTO with additional fields.
     *
     * @param id the ID of the recipe
     * @param title the title of the recipe
     * @param description the description of the recipe
     * @param imgUri the image URI of the recipe
     * @param isPrivate the privacy status of the recipe
     * @param creationDate the creation date of the recipe
     * @param ownerUri the owner URI of the recipe
     * @param likedByUserUris the list of user URIs who liked the recipe
     * @param productUris the list of product URIs associated with the recipe
     * @param productQuantities the map of product names to their quantities
     */
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

    /**
     * Gets the ID of the recipe.
     *
     * @return the ID of the recipe
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the ID of the recipe.
     *
     * @param id the ID of the recipe
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the title of the recipe.
     *
     * @return the title of the recipe
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the recipe.
     *
     * @param title the title of the recipe
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the image URI of the recipe.
     *
     * @return the image URI of the recipe
     */
    public String getImgUri() {
        return imgUri;
    }

    /**
     * Sets the image URI of the recipe.
     *
     * @param imgUri the image URI of the recipe
     */
    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    /**
     * Gets the description of the recipe.
     *
     * @return the description of the recipe
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the recipe.
     *
     * @param description the description of the recipe
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the creation date of the recipe.
     *
     * @return the creation date of the recipe
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * Sets the creation date of the recipe.
     *
     * @param creationDate the creation date of the recipe
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Gets the owner URI of the recipe.
     *
     * @return the owner URI of the recipe
     */
    public String getOwnerUri() {
        return ownerUri;
    }

    /**
     * Sets the owner URI of the recipe.
     *
     * @param ownerUri the owner URI of the recipe
     */
    public void setOwnerUri(String ownerUri) {
        this.ownerUri = ownerUri;
    }

    /**
     * Checks if the recipe is private.
     *
     * @return true if the recipe is private, false otherwise
     */
    public boolean isPrivate() {
        return isPrivate;
    }

    /**
     * Sets the privacy status of the recipe.
     *
     * @param aPrivate the privacy status of the recipe
     */
    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    /**
     * Gets the list of user URIs who liked the recipe.
     *
     * @return the list of user URIs who liked the recipe
     */
    public List<String> getLikedByUserUris() {
        return likedByUserUris;
    }

    /**
     * Sets the list of user URIs who liked the recipe.
     *
     * @param likedByUserUris the list of user URIs who liked the recipe
     */
    public void setLikedByUserUris(List<String> likedByUserUris) {
        this.likedByUserUris = likedByUserUris;
    }

    /**
     * Gets the list of product URIs associated with the recipe.
     *
     * @return the list of product URIs associated with the recipe
     */
    public List<String> getProductUris() {
        return productUris;
    }

    /**
     * Sets the list of product URIs associated with the recipe.
     *
     * @param productUris the list of product URIs associated with the recipe
     */
    public void setProductUris(List<String> productUris) {
        this.productUris = productUris;
    }

    /**
     * Gets the map of product names to their quantities.
     *
     * @return the map of product names to their quantities
     */
    public Map<String, Integer> getProductQuantities() {
        return productQuantities;
    }

    /**
     * Sets the map of product names to their quantities.
     *
     * @param productQuantities the map of product names to their quantities
     */
    public void setProductQuantities(Map<String, Integer> productQuantities) {
        this.productQuantities = productQuantities;
    }
}