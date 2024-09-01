package com.sks.surveys.service.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SurveyServiceTest {

    @Mock
    private SurveyRepository surveyRepository;

    @Mock
    private SurveyParticipantsRepository surveyParticipantsRepository;

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

    @Test
    void getSurveyByIdReturnsSurvey() {
        SurveyEntity survey = new SurveyEntity();
        when(surveyRepository.findById(1L)).thenReturn(Optional.of(survey));

        Optional<SurveyEntity> result = surveyService.getSurveyById(1L);

        assertEquals(survey, result.get());
    }

    @Test
    void getSurveyByIdReturnsEmptyOptionalWhenSurveyNotFound() {
        when(surveyRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<SurveyEntity> result = surveyService.getSurveyById(1L);

        assertEquals(Optional.empty(), result);
    }

    @Test
    void getSurveysByOwnerUriReturnsListOfSurveys() {
        SurveyEntity survey = new SurveyEntity();
        when(surveyRepository.findByOwnerUri("/users/42")).thenReturn(List.of(survey));

        List<SurveyEntity> result = surveyService.getSurveysByOwnerUri("/users/42");

        assertEquals(1, result.size());
        assertEquals(survey, result.getFirst());
    }

    @Test
    void getSurveysByOwnerUriReturnsEmptyListWhenNoSurveys() {
        when(surveyRepository.findByOwnerUri("/users/42")).thenReturn(Collections.emptyList());

        List<SurveyEntity> result = surveyService.getSurveysByOwnerUri("/users/42");

        assertEquals(0, result.size());
    }

    @Test
    void deleteCallsRepositoryDelete() {
        surveyService.delete(1L);

        verify(surveyRepository).deleteById(1L);
    }
    @Test
    void getSurveysByParticipantReturnsListOfSurveys() {
        SurveyEntity survey = new SurveyEntity();
        SurveyParticipants participant = new SurveyParticipants();
        participant.setSurvey(survey);
        when(surveyParticipantsRepository.findByUserUri("/users/42")).thenReturn(Set.of(participant));

        List<SurveyEntity> result = surveyService.getSurveysByParticipant("/users/42");

        assertEquals(1, result.size());
        assertEquals(survey, result.getFirst());
    }

    @Test
    void getSurveysByParticipantReturnsEmptyListWhenNoSurveys() {
        when(surveyParticipantsRepository.findByUserUri("/users/42")).thenReturn(Set.of());

        List<SurveyEntity> result = surveyService.getSurveysByParticipant("/users/42");

        assertEquals(0, result.size());
    }

    @Test
    void getSurveysByParticipantHandlesNullUserUri() {
        List<SurveyEntity> result = surveyService.getSurveysByParticipant(null);

        assertEquals(0, result.size());
    }
}