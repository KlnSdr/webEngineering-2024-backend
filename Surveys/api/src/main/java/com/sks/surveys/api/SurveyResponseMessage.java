package com.sks.surveys.api;

import com.sks.base.api.BaseMessage;

public class SurveyResponseMessage extends BaseMessage {
    private String message;
    private SurveyDTO[] surveys;


    public SurveyResponseMessage() {
    }
    public SurveyResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SurveyDTO[] getSurveys() {
        return surveys;
    }

    public void setSurveys(SurveyDTO[] surveys) {
        this.surveys = surveys;
    }
}
