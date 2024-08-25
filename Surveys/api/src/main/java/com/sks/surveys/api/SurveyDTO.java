package com.sks.surveys.api;

import com.sks.recipes.api.dto.RecipeDTO;

import java.util.Date;
import java.util.Map;

public class SurveyDTO {
    private int id;
    private String title;
    private String[] participants;
    private String creator;
    private Map<RecipeDTO, Integer> RecipeVote;
    private Date creationDate;

    public SurveyDTO() {
    }

    public SurveyDTO(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public SurveyDTO(int id, String title, String[] participants, String creator, Map<RecipeDTO, Integer> RecipeVote, Date creationDate) {
        this.id = id;
        this.title = title;
        this.participants = participants;
        this.creator = creator;
        this.RecipeVote = RecipeVote;
        this.creationDate = creationDate;
    }




    public int getId() {
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

    public String[] getParticipants() {
        return participants;
    }

    public void setParticipants(String[] participants) {
        this.participants = participants;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Map<RecipeDTO, Integer> getRecipeVote() {
        return RecipeVote;
    }

    public void setRecipeVote(Map<RecipeDTO, Integer> recipeVote) {
        RecipeVote = recipeVote;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
