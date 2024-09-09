package com.sks.surveys.service;

import com.sks.surveys.api.*;
import com.sks.surveys.service.data.SurveyEntity;
import com.sks.surveys.service.data.SurveyService;
import com.sks.surveys.service.data.SurveyVote;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Listener class that handles incoming survey-related messages and processes them accordingly.
 */
@Component
public class Listener implements SurveyListener {
    private final SurveySender sender;
    private final SurveyService service;
    private final Jackson2JsonMessageConverter converter;

    /**
     * Constructs a new Listener with the specified dependencies.
     *
     * @param sender the survey sender
     * @param service the survey service
     * @param converter the message converter
     */
    public Listener(SurveySender sender, SurveyService service, @Qualifier("surveysMessageConverter") Jackson2JsonMessageConverter converter) {
        this.sender = sender;
        this.service = service;
        this.converter = converter;
    }

    /**
     * Listens for incoming messages and processes them based on the request type.
     *
     * @param message the incoming message
     */
    @Override
    public void listen(Message message) {
        final Object obj = converter.fromMessage(message);
        if (!(obj instanceof SurveyRequestMessage request)) {
            return;
        }
        final SurveyResponseMessage response = switch (request.getRequestType()) {
            case GET_SurveyById -> getSurveyById(request.getSurveyId());
            case GET_SurveyByOwner -> getSurveysByOwner(request.getOwnerUri());
            case POST_SaveSurvey -> handleCreateSurvey(request.getSurvey());
            case DELETE_DeleteSurvey -> handleDeleteSurvey(request.getSurveyId());
            case PUT_UpdateSurvey -> handleUpdateSurvey(request.getSurvey());
            case PUT_VoteSurvey -> handleVoteSurvey(request.getSurveyId(), request.getRecipeUri(), request.getUserUri());
        };

        sender.sendResponse(request, (response));
    }

    /**
     * Retrieves a survey by its ID.
     *
     * @param surveyId the ID of the survey
     * @return the response message containing the survey
     */
    private SurveyResponseMessage getSurveyById(long surveyId) {
        final SurveyResponseMessage response = new SurveyResponseMessage();
        final List<SurveyDTO> survey = new ArrayList<>();
        Optional<SurveyEntity> surveyEntity = service.getSurveyById(surveyId);
        if (surveyEntity.isPresent()) {
            survey.add(map(surveyEntity.get()));
            response.setSurveys(survey.toArray(new SurveyDTO[0]));
        } else {
            response.setSurveys(new SurveyDTO[0]);
        }
        return response;
    }

    /**
     * Retrieves surveys by the owner's URI.
     *
     * @param ownerUri the URI of the owner
     * @return the response message containing the surveys
     */
    private SurveyResponseMessage getSurveysByOwner(String ownerUri) {
        final SurveyResponseMessage response = new SurveyResponseMessage();
        List<SurveyEntity> surveyEntities = service.getSurveysByOwnerUri(ownerUri);
        response.setSurveys(surveyEntities.stream().map(this::map).toArray(SurveyDTO[]::new));

        return response;
    }

    /**
     * Handles the creation of a new survey.
     *
     * @param survey the survey data transfer object
     * @return the response message containing the created survey
     */
    private SurveyResponseMessage handleCreateSurvey(SurveyDTO survey) {
        SurveyResponseMessage response = new SurveyResponseMessage();
        SurveyEntity entity = new SurveyEntity();
        entity.setTitle(survey.getTitle());
        entity.setOwnerUri(survey.getCreator());
        entity.setCreationDate(survey.getCreationDate());
        entity.setVotes(new HashSet<>());
        entity.setOptions(survey.getOptions());
        try {
            service.save(entity);
            response.setSurveys(new SurveyDTO[]{map(entity)});
        } catch (Exception e) {
            response.setMessage("Error saving survey");
        }
        return response;
    }

    /**
     * Handles the deletion of a survey by its ID.
     *
     * @param surveyId the ID of the survey
     * @return the response message indicating the result of the deletion
     */
    private SurveyResponseMessage handleDeleteSurvey(long surveyId) {
        SurveyResponseMessage response = new SurveyResponseMessage();
        try {
            service.delete(surveyId);
        } catch (Exception e) {
            response.setMessage("Survey with id " + surveyId + " could not be deleted");
        }
        return response;
    }

    /**
     * Handles the update of an existing survey.
     *
     * @param survey the survey data transfer object
     * @return the response message containing the updated survey
     */
    private SurveyResponseMessage handleUpdateSurvey(SurveyDTO survey) {
        SurveyResponseMessage response = new SurveyResponseMessage();
        Optional<SurveyEntity> entity = service.getSurveyById(survey.getId());
        if (entity.isPresent()) {
            SurveyEntity updatedEntity = new SurveyEntity();
            updatedEntity.setId(survey.getId());
            updatedEntity.setTitle(survey.getTitle());
            updatedEntity.setOwnerUri(survey.getCreator());
            updatedEntity.setCreationDate((entity.get().getCreationDate()));
            updatedEntity.setVotes(new HashSet<>());
            updatedEntity.setOptions(survey.getOptions());
            try {
                service.save(updatedEntity);
                response.setSurveys(new SurveyDTO[]{map(updatedEntity)});
            } catch (Exception e) {
                response.setMessage("Error updating survey");
            }
        }
        return response;
    }

    /**
     * Handles voting on a survey.
     *
     * @param surveyId the ID of the survey
     * @param recipeUri the URI of the recipe being voted on
     * @param userUri the URI of the user voting
     * @return the response message containing the updated survey
     */
    private SurveyResponseMessage handleVoteSurvey(long surveyId, String recipeUri, String userUri) {
        Optional<SurveyEntity> entity = service.getSurveyById(surveyId);
        SurveyResponseMessage response = new SurveyResponseMessage();
        SurveyVote newVote = new SurveyVote();
        if (entity.isPresent()) {
            SurveyEntity currentEntity = entity.get();
            if (!entity.get().getOptions().contains(recipeUri)) {
                response.setMessage("Recipe not found in survey options");
                return response;
            }

            currentEntity.getVotes().stream()
                    .filter(vote -> vote.getUserUri().equals(userUri))
                    .findFirst().ifPresent(existingVote -> currentEntity.getVotes().remove(existingVote));

            newVote.setRecipeUri(recipeUri);
            newVote.setUserUri(userUri);
            newVote.setSurvey(currentEntity);
            SurveyEntity updatedEntity = entity.get();
            updatedEntity.getVotes().add(newVote);
            try {
                service.save(updatedEntity);
                response.setSurveys(new SurveyDTO[]{map(updatedEntity)});
            } catch (Exception e) {
                response.setMessage("Error voting on survey");
            }
        }

        return response;
    }

    /**
     * Maps a SurveyEntity to a SurveyDTO.
     *
     * @param entity the survey entity
     * @return the survey data transfer object
     */
    private SurveyDTO map(SurveyEntity entity) {
        Map<String, Integer> recipeVotes = new HashMap<>();
        for (SurveyVote vote : entity.getVotes()) {
            recipeVotes.put(vote.getRecipeUri(), recipeVotes.getOrDefault(vote.getRecipeUri(), 0) + 1);
        }
        return new SurveyDTO(entity.getId(), entity.getTitle(), entity.getOwnerUri(), recipeVotes, entity.getOptions(), entity.getCreationDate());
    }
}