package com.sks.surveys.service.data;

import jakarta.persistence.*;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "surveys_entity_table")
public class SurveyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "surveys_id")
    private Long id;

    @Column(name = "owner_uri")
    private String ownerUri;  // User URI as a string

    @ElementCollection
    @CollectionTable(name = "survey_recipe_votes", joinColumns = @JoinColumn(name = "survey_id"))
    @MapKeyColumn(name = "recipe_uri")
    @Column(name = "yes_votes")
    private Map<String, Integer> recipeYesVotes = new HashMap<>();  // Map of Recipe URIs to yesVotes

    public String getOwnerUri() {
        return ownerUri;
    }

    public void setOwnerUri(String ownerUri) {
        this.ownerUri = ownerUri;
    }

    public Map<String, Integer> getRecipeYesVotes() {
        return recipeYesVotes;
    }

    public void setRecipeYesVotes(Map<String, Integer> recipeYesVotes) {
        this.recipeYesVotes = recipeYesVotes;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}