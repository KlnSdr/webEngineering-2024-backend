package com.sks.surveys.service.data;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SurveyService {
    private final SurveyRepository surveysRepository;
    private final SurveyParticipantsRepository surveyParticipantsRepository;

    public SurveyService(SurveyRepository surveysRepository, SurveyParticipantsRepository surveyParticipantsRepository) {
        this.surveysRepository = surveysRepository;
        this.surveyParticipantsRepository = surveyParticipantsRepository;
    }

    public Optional<SurveyEntity> getSurveyById(long surveyId) {
        return surveysRepository.findById(surveyId);
    }

    public List<SurveyEntity> getSurveysByOwnerUri(String ownerUri) {
        return surveysRepository.findByOwnerUri(ownerUri);
    }

    public List<SurveyEntity> getAll() {
        return surveysRepository.findAll();
    }

    public SurveyEntity save(SurveyEntity surveysEntity) {
        return surveysRepository.save(surveysEntity);
    }
    public void delete(long surveyId) {
        surveysRepository.deleteById(surveyId);
    }

    public List<SurveyEntity> getSurveysByParticipant(String userUri) {
        return surveyParticipantsRepository.findByUserUri(userUri).stream().map(SurveyParticipants::getSurvey).toList();
    }
}
