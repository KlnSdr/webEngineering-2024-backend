package com.sks.recipes.api.dto;

import java.util.Map;

/**
 * Data Transfer Object (DTO) for creating a recipe.
 */
public class CreateRecipeDTO {
    private long id = -1;
    private String title;
    private String imgUri;
    private String description;
    private String ownerUri;
    private boolean isPrivate;
    private Map<String, Integer> productQuantities;

    /**
     * Default constructor for CreateRecipeDTO.
     */
    public CreateRecipeDTO() {
    }

    /**
     * Parameterized constructor for CreateRecipeDTO.
     *
     * @param title the title of the recipe
     * @param description the description of the recipe
     * @param imgUri the image URI of the recipe
     * @param ownerUri the owner URI of the recipe
     */
    public CreateRecipeDTO(String title, String description, String imgUri, String ownerUri) {
        this.title = title;
        this.description = description;
        this.imgUri = imgUri;
        this.ownerUri = ownerUri;
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
    public void setIsPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    /**
     * Gets the product quantities for the recipe.
     *
     * @return a map of product names to their quantities
     */
    public Map<String, Integer> getProductQuantities() {
        return productQuantities;
    }

    /**
     * Sets the product quantities for the recipe.
     *
     * @param productQuantities a map of product names to their quantities
     */
    public void setProductQuantities(Map<String, Integer> productQuantities) {
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
    public void setId(long id) {
        this.id = id;
    }
}