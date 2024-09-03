package com.sks.surveys.service.data;

import com.sks.surveys.api.*;
import com.sks.surveys.service.Listener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ListernerTest {

    @Mock
    private SurveySender sender;

    @Mock
    private SurveyService surveyService;

    @Mock
    private Jackson2JsonMessageConverter converter;

    private Listener listener;

    @BeforeEach
    void setUp() {
        sender = mock(SurveySender.class);
        surveyService = mock(SurveyService.class);
        converter = mock(Jackson2JsonMessageConverter.class);
        listener = new Listener(sender, surveyService, converter);
    }

    @Test
    void getSurveyByIdRequestTest() {
        SurveyRequestMessage request = new SurveyRequestMessage();
        request.setRequestType(SurveyRequestType.GET_SurveyById);
        request.setSurveyId(1L);
        SurveyEntity surveyEntity = new SurveyEntity();
        surveyEntity.setId(1L);
        when(surveyService.getSurveyById(1L)).thenReturn(Optional.of(surveyEntity));
        when(converter.fromMessage(any())).thenReturn(request);

        listener.listen(null);

        verify(sender).sendResponse(eq(request), any(SurveyResponseMessage.class));
    }

    @Test
    void getSurveyByIdRequestNotFoundTest() {
        SurveyRequestMessage request = new SurveyRequestMessage();
        request.setRequestType(SurveyRequestType.GET_SurveyById);
        request.setSurveyId(1L);
        when(surveyService.getSurveyById(1L)).thenReturn(Optional.empty());
        when(converter.fromMessage(any())).thenReturn(request);

        listener.listen(null);

        verify(sender).sendResponse(eq(request), any(SurveyResponseMessage.class));
    }

    @Test
    void getSurveysByOwnerRequestTest() {
        SurveyRequestMessage request = new SurveyRequestMessage();
        request.setRequestType(SurveyRequestType.GET_SurveyByOwner);
        request.setOwnerUri("/users/42");
        when(converter.fromMessage(any())).thenReturn(request);

        listener.listen(null);

        verify(sender).sendResponse(eq(request), any(SurveyResponseMessage.class));
    }

    @Test
    void handleSaveSurveyRequestTest() {
        SurveyRequestMessage request = new SurveyRequestMessage();
        request.setRequestType(SurveyRequestType.POST_SaveSurvey);

        SurveyDTO survey = new SurveyDTO();
        survey.setTitle("title");
        survey.setOptions(List.of("/recipes/1", "recepies/2"));
        survey.setCreator("/users/42");
        survey.setRecipeVote(null);
        survey.setCreationDate(null);

        request.setSurvey(survey);
        SurveyEntity surveyEntity = new SurveyEntity();
        surveyEntity.setTitle("title");
        surveyEntity.setOwnerUri("/users/42");
        surveyEntity.setCreationDate(null);
        surveyEntity.setVotes(Set.of());
        surveyEntity.setOptions(List.of("/recipes/1", "recepies/2"));


        when((surveyService.save(any()))).thenReturn(surveyEntity);
        when(converter.fromMessage(any())).thenReturn(request);

        listener.listen(null);

        verify(sender).sendResponse(eq(request), any(SurveyResponseMessage.class));
    }

    @Test
    void handleDeleteSurveyRequestTest() {
        SurveyRequestMessage request = new SurveyRequestMessage();
        request.setRequestType(SurveyRequestType.DELETE_DeleteSurvey);
        request.setSurveyId(1L);
        SurveyEntity surveyEntity = new SurveyEntity();
        surveyEntity.setId(1L);
        when(surveyService.getSurveyById(1L)).thenReturn(Optional.of(surveyEntity));
        when(converter.fromMessage(any())).thenReturn(request);

        listener.listen(null);

        verify(sender).sendResponse(eq(request), any(SurveyResponseMessage.class));
    }

    @Test
    void handleUpdateSurveyRequestTest() {
        SurveyRequestMessage request = new SurveyRequestMessage();
        request.setRequestType(SurveyRequestType.PUT_UpdateSurvey);

        SurveyDTO survey = new SurveyDTO();
        survey.setId(1L);
        survey.setTitle("title");
        survey.setOptions(List.of("/recipes/1", "recepies/2"));
        survey.setCreator("/users/42");
        survey.setRecipeVote(null);
        survey.setCreationDate(null);

        request.setSurvey(survey);

        SurveyEntity surveyEntity = new SurveyEntity();
        surveyEntity.setId(1L);
        surveyEntity.setTitle("title");
        surveyEntity.setOwnerUri("/users/42");
        surveyEntity.setCreationDate(null);
        surveyEntity.setVotes(Set.of());
        surveyEntity.setOptions(List.of("/recipes/1", "recepies/2"));

        when(surveyService.getSurveyById(1L)).thenReturn(Optional.of(surveyEntity));
        when((surveyService.save(any()))).thenReturn(surveyEntity);
        when(converter.fromMessage(any())).thenReturn(request);

        listener.listen(null);

        verify(sender).sendResponse(eq(request), any(SurveyResponseMessage.class));
    }
}


