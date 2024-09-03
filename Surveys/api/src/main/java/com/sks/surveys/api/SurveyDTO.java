package com.sks.surveys.api;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class SurveyDTO {
    private long id;
    private String title;
    private String creator;
    private Map<String, Integer> recipeVote;
    private List<String> options;
    private Date creationDate;

    public SurveyDTO() {
    }

    public SurveyDTO(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public SurveyDTO(long id, String title, String creator, Map<String, Integer> RecipeVote,List<String> option, Date creationDate) {
        this.id = id;
        this.title = title;
        this.creator = creator;
        this.recipeVote = RecipeVote;
        this.options = option;
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

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Map<String, Integer> getRecipeVote() {
        return recipeVote;
    }

    public void setRecipeVote(Map<String, Integer> recipeVote) {
        this.recipeVote = recipeVote;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
}
