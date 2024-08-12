package com.sks.surveys.service.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class SurveyServiceTest {

    @Mock
    private SurveyRepository surveyRepository;

    @InjectMocks
    private SurveyService surveyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllReturnsListOfSurveys() {
        SurveyEntity survey = new SurveyEntity();
        when(surveyRepository.findAll()).thenReturn(List.of(survey));

        List<SurveyEntity> result = surveyService.getAll();

        assertEquals(1, result.size());
        assertEquals(survey, result.getFirst());
    }

    @Test
    void getAllReturnsEmptyListWhenNoSurveys() {
        when(surveyRepository.findAll()).thenReturn(Collections.emptyList());

        List<SurveyEntity> result = surveyService.getAll();

        assertEquals(0, result.size());
    }

    @Test
    void saveReturnsSavedSurvey() {
        SurveyEntity survey = new SurveyEntity();
        when(surveyRepository.save(survey)).thenReturn(survey);

        SurveyEntity result = surveyService.save(survey);

        assertEquals(survey, result);
    }
}