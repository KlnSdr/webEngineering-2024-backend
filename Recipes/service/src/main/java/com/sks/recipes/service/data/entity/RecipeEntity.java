package com.sks.recipes.service.data.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * Entity class representing a recipe.
 */
@Entity
@Table(name = "recipes")
public class RecipeEntity {

    /**
     * The unique identifier for the recipe.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RECIPE_ID")
    private Long id;

    /**
     * The title of the recipe.
     */
    @Column(name = "RECIPE_TITLE")
    private String title;

    /**
     * The description of the recipe.
     */
    @Column(name = "RECIPE_DESCRIPTION", length = 10000)
    private String description;

    /**
     * The URI of the image associated with the recipe.
     */
    @Column(name = "RECIPE_IMAGE_URI")
    private String imageUri;

    /**
     * Indicates whether the recipe is private.
     */
    @Column(name = "RECIPE_IS_PRIVATE")
    private boolean isPrivate;

    /**
     * The creation date of the recipe.
     * This field is not nullable, not updatable, and has a default value of the current timestamp.
     */
    @Column(name = "RECIPE_CREATION_DATE", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp creationDate;

    /**
     * The URI of the owner of the recipe.
     */
    @Column(name = "RECIPE_OWNER_URI")
    private String ownerUri;

    /**
     * The list of URIs of users who liked the recipe.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "recipe_likes", joinColumns = @JoinColumn(name = "RECIPE_ID"))
    @Column(name = "USER_URI")
    private List<String> likedByUserUris;

    /**
     * The list of URIs of products used in the recipe.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "recipe_products", joinColumns = @JoinColumn(name = "RECIPE_ID"))
    @Column(name = "PRODUCT_URI")
    private List<String> productUris;

    /**
     * The map of product URIs to their quantities used in the recipe.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "recipe_product_quantities", joinColumns = @JoinColumn(name = "RECIPE_ID"))
    @MapKeyColumn(name = "PRODUCT_URI")
    @Column(name = "QUANTITY")
    private Map<String, Integer> productQuantities;

    /**
     * Default constructor.
     */
    public RecipeEntity() {
    }

    /**
     * Constructor with all fields.
     *
     * @param id the unique identifier for the recipe
     * @param title the title of the recipe
     * @param description the description of the recipe
     * @param imageUri the URI of the image associated with the recipe
     * @param isPrivate indicates whether the recipe is private
     * @param creationDate the creation date of the recipe
     * @param ownerUri the URI of the owner of the recipe
     * @param likedByUserUris the list of URIs of users who liked the recipe
     * @param productUris the list of URIs of products used in the recipe
     * @param productQuantities the map of product URIs to their quantities used in the recipe
     */
    public RecipeEntity(long id, String title, String description, String imageUri, boolean isPrivate, Timestamp creationDate, String ownerUri, List<String> likedByUserUris, List<String> productUris, Map<String, Integer> productQuantities) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUri = imageUri;
        this.isPrivate = isPrivate;
        this.creationDate = creationDate;
        this.ownerUri = ownerUri;
        this.likedByUserUris = likedByUserUris;
        this.productUris = productUris;
        this.productQuantities = productQuantities;
    }

    /**
     * Gets the list of URIs of users who liked the recipe.
     *
     * @return the list of URIs of users who liked the recipe
     */
    public List<String> getLikedByUserUris() {
        return likedByUserUris;
    }

    /**
     * Sets the list of URIs of users who liked the recipe.
     *
     * @param likedByUserUris the list of URIs of users who liked the recipe
     */
    public void setLikedByUserUris(List<String> likedByUserUris) {
        this.likedByUserUris = likedByUserUris;
    }

    /**
     * Gets the list of URIs of products used in the recipe.
     *
     * @return the list of URIs of products used in the recipe
     */
    public List<String> getProductUris() {
        return productUris;
    }

    /**
     * Sets the list of URIs of products used in the recipe.
     *
     * @param productUris the list of URIs of products used in the recipe
     */
    public void setProductUris(List<String> productUris) {
        this.productUris = productUris;
    }

    /**
     * Gets the map of product URIs to their quantities used in the recipe.
     *
     * @return the map of product URIs to their quantities used in the recipe
     */
    public Map<String, Integer> getProductQuantities() {
        return productQuantities;
    }

    /**
     * Sets the map of product URIs to their quantities used in the recipe.
     *
     * @param productQuantities the map of product URIs to their quantities used in the recipe
     */
    public void setProductQuantities(Map<String, Integer> productQuantities) {
        this.productQuantities = productQuantities;
    }

    /**
     * Gets the URI of the owner of the recipe.
     *
     * @return the URI of the owner of the recipe
     */
    public String getOwnerUri() {
        return ownerUri;
    }

    /**
     * Sets the URI of the owner of the recipe.
     *
     * @param ownerUri the URI of the owner of the recipe
     */
    public void setOwnerUri(String ownerUri) {
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
     * Gets the URI of the image associated with the recipe.
     *
     * @return the URI of the image associated with the recipe
     */
    public String getImageUri() {
        return imageUri;
    }

    /**
     * Sets the URI of the image associated with the recipe.
     *
     * @param imageUri the URI of the image associated with the recipe
     */
    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
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
     * Gets the creation date of the recipe.
     *
     * @return the creation date of the recipe
     */
    public Timestamp getCreationDate() {
        return creationDate;
    }

    /**
     * Sets the creation date of the recipe.
     *
     * @param creationDate the creation date of the recipe
     */
    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Gets the unique identifier for the recipe.
     *
     * @return the unique identifier for the recipe
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the recipe.
     *
     * @param id the unique identifier for the recipe
     */
    public void setId(Long id) {
        this.id = id;
    }
}