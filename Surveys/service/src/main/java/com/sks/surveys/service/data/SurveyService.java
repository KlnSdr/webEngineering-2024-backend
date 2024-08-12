package com.sks.surveys.service.data;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SurveyService {
    private SurveyRepository surveysRepository;

    public List<SurveyEntity> getAll() {
        return surveysRepository.findAll();
    }

    public SurveyEntity save(SurveyEntity surveysEntity) {
        return surveysRepository.save(surveysEntity);
    }
}
