package com.sks.surveys.api;

import com.sks.base.api.BaseSender;

public interface SurveySender extends BaseSender<SurveyRequestMessage, SurveyResponseMessage> {
    SurveyResponseMessage sendRequest(SurveyRequestMessage message);
    void sendResponse(SurveyRequestMessage request, SurveyResponseMessage response);
}
