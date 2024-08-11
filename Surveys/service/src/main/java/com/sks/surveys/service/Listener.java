package com.sks.surveys.service;

import com.sks.surveys.api.SurveyListener;
import com.sks.surveys.api.SurveyRequestMessage;
import com.sks.surveys.api.SurveyResponseMessage;
import com.sks.surveys.api.SurveySender;
import org.springframework.stereotype.Component;

@Component
public class Listener implements SurveyListener {
    private final SurveySender sender;

    public Listener(SurveySender sender) {
        this.sender = sender;
    }

    @Override
    public void listen(SurveyRequestMessage message) {
        sender.sendResponse(message, new SurveyResponseMessage("Listener got message: " + message.getMessage()));
    }
}
