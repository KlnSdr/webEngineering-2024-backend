package com.sks.surveys.service;

import com.sks.surveys.api.*;
import com.sks.surveys.service.data.SurveyEntity;
import com.sks.surveys.service.data.SurveyParticipants;
import com.sks.surveys.service.data.SurveyService;
import com.sks.surveys.service.data.SurveyVote;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Listener implements SurveyListener {
    private final SurveySender sender;
    private final SurveyService service;
    private final Jackson2JsonMessageConverter converter;

    public Listener(SurveySender sender, SurveyService service, @Qualifier("surveysMessageConverter") Jackson2JsonMessageConverter converter) {
        this.sender = sender;
        this.service = service;
        this.converter = converter;
    }

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

    private SurveyResponseMessage getSurveyById(long surveyId) {
        final SurveyResponseMessage response = new SurveyResponseMessage();
        final List<SurveyDTO> survey = new ArrayList<>();
        Optional<SurveyEntity> surveyEntity = service.getSurveyById(surveyId);
        if (surveyEntity.isPresent()) {
            survey.add(map(surveyEntity.get()));
            response.setSurveys(survey.toArray(new SurveyDTO[0]));
        }else{
            response.setSurveys(new SurveyDTO[0]);
        }
        return response;
    }

    private SurveyResponseMessage getSurveysByOwner(String ownerUri) {
        final SurveyResponseMessage response = new SurveyResponseMessage();
        List<SurveyEntity> surveyEntities = service.getSurveysByOwnerUri(ownerUri);
        if (surveyEntities.isEmpty()) {
            response.setSurveys(new SurveyDTO[0]);
        }else{
           response.setSurveys(surveyEntities.stream().map(this::map).toArray(SurveyDTO[]::new));
        }
        return response;
    }

    private SurveyResponseMessage handleCreateSurvey(SurveyDTO survey) {
        SurveyResponseMessage response = new SurveyResponseMessage();
        SurveyEntity entity = new SurveyEntity();
        entity.setTitle(survey.getTitle());
        entity.setOwnerUri(survey.getCreator());
        entity.setCreationDate(survey.getCreationDate());
        Set<SurveyParticipants> participants = new HashSet<>();
        for (String participant : survey.getParticipants()) {
            participants.add(new SurveyParticipants(entity, participant));
        }
        entity.setParticipants(participants);
        entity.setVotes(new HashSet<>());
        entity.setOptions(survey.getOptions());
        try{
            service.save(entity);
            response.setSurveys(new SurveyDTO[]{map(entity)});
        }catch (Exception e){
            response.setMessage("Error saving survey");
        }
        return response;

    }

    private SurveyResponseMessage handleDeleteSurvey(long surveyId){
        SurveyResponseMessage response = new SurveyResponseMessage();
        try{
            service.delete(surveyId);
        }catch (Exception e){
            response.setMessage("Survey with id " + surveyId + " could not be deleted");
        }
        return response;
    }

    private SurveyResponseMessage handleUpdateSurvey(SurveyDTO survey) {
        SurveyResponseMessage response = new SurveyResponseMessage();
        Optional<SurveyEntity> entity = service.getSurveyById(survey.getId());
        if (entity.isPresent()) {
            SurveyEntity updatedEntity = new SurveyEntity();
            updatedEntity.setId(survey.getId());
            updatedEntity.setTitle(survey.getTitle());
            updatedEntity.setOwnerUri(survey.getCreator());
            updatedEntity.setCreationDate((entity.get().getCreationDate()));
            Set<SurveyParticipants> participants = new HashSet<>();
            for (String participant : survey.getParticipants()) {
                participants.add(new SurveyParticipants(updatedEntity, participant));
            }
            updatedEntity.setParticipants(participants);
            updatedEntity.setVotes(new HashSet<>());
            updatedEntity.setOptions(survey.getOptions());
            try{
                service.save(updatedEntity);
                response.setSurveys(new SurveyDTO[]{map(updatedEntity)});
            }catch (Exception e){
                response.setMessage("Error updating survey");
            }
        }
        return response;
    }

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
            try{
                service.save(updatedEntity);
                response.setSurveys(new SurveyDTO[]{map(updatedEntity)});
            }catch (Exception e){
                response.setMessage("Error voting on survey");
            }
        }

        return response;
    }
    private SurveyDTO map(SurveyEntity entity) {

        String[] participants = entity.getParticipants().stream()
                .map(SurveyParticipants::getUserUri)
                .toArray(String[]::new);
        Map<String, Integer> recipeVotes = new HashMap<>();
         for (SurveyVote vote : entity.getVotes()) {
            recipeVotes.put(vote.getRecipeUri(), recipeVotes.getOrDefault(vote.getRecipeUri(), 0) + 1);
         }
         return new SurveyDTO(entity.getId(), entity.getTitle(), participants, entity.getOwnerUri(), recipeVotes, entity.getOptions(), entity.getCreationDate());
    }

}
