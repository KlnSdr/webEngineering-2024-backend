package com.sks.surveys.api;

import com.sks.base.api.BaseMessage;

public class SurveyRequestMessage extends BaseMessage {
    private String message;
    private String recipeUri;
    private long surveyId;
    private RequestType requestType;
    private SurveyDTO survey;
    private String ownerUri;
    private String userUri;

    public SurveyRequestMessage() {
    }

    public SurveyRequestMessage(long surveyId, RequestType requestType) {
        this.surveyId = surveyId;
        this.requestType = requestType;
    }
    public SurveyRequestMessage(SurveyDTO survey, RequestType requestType) {
        this.survey = survey;
        this.requestType = requestType;
    }

    public SurveyRequestMessage(String ownerUri, RequestType requestType) {
        this.ownerUri = ownerUri;
        this.requestType = requestType;
    }

    public SurveyRequestMessage(String recipeUri, String userUri, long surveyId, RequestType requestType) {
        this.recipeUri = recipeUri;
        this.userUri = userUri;
        this.surveyId = surveyId;
        this.requestType = requestType;
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

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
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
