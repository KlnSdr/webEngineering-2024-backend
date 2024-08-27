package com.sks.surveys.api;

import java.util.Date;
import java.util.Map;

public class SurveyDTO {
    private long id;
    private String title;
    private String[] participants;
    private String creator;
    private Map<String, Integer> RecipeVote;
    private Date creationDate;

    public SurveyDTO() {
    }

    public SurveyDTO(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public SurveyDTO(long id, String title, String[] participants, String creator, Map<String, Integer> RecipeVote, Date creationDate) {
        this.id = id;
        this.title = title;
        this.participants = participants;
        this.creator = creator;
        this.RecipeVote = RecipeVote;
        this.creationDate = creationDate;
    }




    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public Map<String, Integer> getRecipeVote() {
        return RecipeVote;
    }

    public void setRecipeVote(Map<String, Integer> recipeVote) {
        RecipeVote = recipeVote;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
