package com.sks.surveys.api;

import com.sks.base.api.BaseMessage;

public class SurveyRequestMessage extends BaseMessage {
    private String message;

    public SurveyRequestMessage() {
    }

    public SurveyRequestMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
