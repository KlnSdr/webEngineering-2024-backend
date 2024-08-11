package com.sks.surveys.service;

import com.sks.surveys.api.*;
import com.sks.surveys.service.data.SurveyEntity;
import com.sks.surveys.service.data.SurveyRepository;
import org.springframework.stereotype.Component;

@Component
public class Listener implements SurveyListener {
    private final SurveySender sender;
    private final SurveyRepository repo;

    public Listener(SurveySender sender, SurveyRepository repo) {
        this.sender = sender;
        this.repo = repo;
    }

    @Override
    public void listen(SurveyRequestMessage message) {
        final SurveyEntity entity = new SurveyEntity();
        entity.setName(message.getMessage());
        repo.save(entity);

        repo.findAll().forEach(surveysEntity -> System.out.println(surveysEntity.getName()));
        sender.sendResponse(message, new SurveyResponseMessage("Listener got message: " + message.getMessage()));
    }
}
