package com.sks.surveys.api;

import com.sks.base.api.BaseMessage;

public class SurveyResponseMessage extends BaseMessage {
    private String message;
    SurveyDTO[] survey;

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

    public SurveyDTO[] getSurvey() {
        return survey;
    }

    public void setSurvey(SurveyDTO[] survey) {
        this.survey = survey;
    }
}
