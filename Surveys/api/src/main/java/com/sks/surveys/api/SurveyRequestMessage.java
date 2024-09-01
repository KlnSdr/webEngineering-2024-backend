package com.sks.surveys.api;

import com.sks.base.api.BaseMessage;

public class SurveyRequestMessage extends BaseMessage {
    private String message;
    private String recipeUri;
    private long surveyId;
    private SurveyRequestType surveyRequestType;
    private SurveyDTO survey;
    private String ownerUri;
    private String userUri;

    public SurveyRequestMessage() {
    }

    public SurveyRequestMessage(long surveyId, SurveyRequestType surveyRequestType) {
        this.surveyId = surveyId;
        this.surveyRequestType = surveyRequestType;
    }
    public SurveyRequestMessage(SurveyDTO survey, SurveyRequestType surveyRequestType) {
        this.survey = survey;
        this.surveyRequestType = surveyRequestType;
    }

    public SurveyRequestMessage(String ownerUri, SurveyRequestType surveyRequestType) {
        this.ownerUri = ownerUri;
        this.surveyRequestType = surveyRequestType;
    }

    public SurveyRequestMessage(String recipeUri, String userUri, long surveyId, SurveyRequestType surveyRequestType) {
        this.recipeUri = recipeUri;
        this.userUri = userUri;
        this.surveyId = surveyId;
        this.surveyRequestType = surveyRequestType;
    }

    public static SurveyRequestMessage getParticipating(String userUri) {
        final SurveyRequestMessage surveyRequestMessage = new SurveyRequestMessage();
        surveyRequestMessage.setUserUri(userUri);
        surveyRequestMessage.setRequestType(SurveyRequestType.GET_SurveysByParticipant);
        return surveyRequestMessage;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(long surveyId) {
        this.surveyId = surveyId;
    }

    public SurveyRequestType getRequestType() {
        return surveyRequestType;
    }

    public void setRequestType(SurveyRequestType surveyRequestType) {
        this.surveyRequestType = surveyRequestType;
    }

    public SurveyDTO getSurvey() {
        return survey;
    }

    public void setSurvey(SurveyDTO survey) {
        this.survey = survey;
    }

    public String getOwnerUri() {
        return ownerUri;
    }

    public void setOwnerUri(String ownerUri) {
        this.ownerUri = ownerUri;
    }

    public String getRecipeUri() {
        return recipeUri;
    }

    public void setRecipeUri(String recipeUri) {
        this.recipeUri = recipeUri;
    }

    public String getUserUri() {
        return userUri;
    }

    public void setUserUri(String userUri) {
        this.userUri = userUri;
    }
}
