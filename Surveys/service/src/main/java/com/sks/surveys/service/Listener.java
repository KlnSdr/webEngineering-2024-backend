package com.sks.surveys.service;

import com.sks.surveys.api.*;
import com.sks.surveys.service.data.SurveyEntity;
import com.sks.surveys.service.data.SurveyParticipants;
import com.sks.surveys.service.data.SurveyService;
import com.sks.surveys.service.data.SurveyVote;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.*;

@Component
public class Listener implements SurveyListener {
    private final SurveySender sender;
    private final SurveyService service;

    public Listener(SurveySender sender, SurveyService service) {
        this.sender = sender;
        this.service = service;
    }

    @Override
    public void listen(SurveyRequestMessage message) {
        final SurveyResponseMessage response = switch (message.getRequestType()) {
            case SurveyById -> getSurveyById(message.getsurveyId());
            case SurveyByOwner -> getSurveysByOwner(message.getOwnerUri());
            case SaveSurvey -> handleSaveSurvey(message.getSurvey());
            case DeleteSurvey -> handleDeleteSurvey(message.getsurveyId());
            case UpdateSurvey -> handleUpdateSurvey(message.getSurvey());
            case VoteSurvey -> handleVoteSurvey(message.getsurveyId(), message.getRecipeUri(), message.getUserUri());
        };

        sender.sendResponse(message, (response));
    }

    private SurveyResponseMessage getSurveyById(long surveyId) {
        final SurveyResponseMessage response = new SurveyResponseMessage();
        final List<SurveyDTO> survey = new ArrayList<>();
        Optional<SurveyEntity> surveyEntity = service.getSurveyById(surveyId);
        if (surveyEntity.isPresent()) {
            survey.add(map(surveyEntity.get()));
            response.setSurvey(survey.toArray(new SurveyDTO[0]));
        }else{
            response.setMessage("Survey not found");
        }
        return response;
    }

    private SurveyResponseMessage getSurveysByOwner(String ownerUri) {
        final SurveyResponseMessage response = new SurveyResponseMessage();
        List<SurveyEntity> surveyEntities = service.getSurveysByOwnerUri(ownerUri);
        if (surveyEntities.isEmpty()) {
            response.setMessage("No surveys found");
        }else{
           response.setSurvey(surveyEntities.stream().map(this::map).toArray(SurveyDTO[]::new));
        }
        return response;
    }

    private SurveyResponseMessage handleSaveSurvey(SurveyDTO survey) {
        SurveyResponseMessage response = new SurveyResponseMessage();
        SurveyEntity entity = new SurveyEntity();
        entity.setTitle(survey.getTitle());
        entity.setOwnerUri(survey.getCreator());
        entity.setCreationDate((Timestamp) survey.getCreationDate());
        Set<SurveyParticipants> participants = new HashSet<>();
        for (String participant : survey.getParticipants()) {
            participants.add(new SurveyParticipants(entity, participant));
        }
        entity.setParticipants(participants);
        entity.setVotes(new HashSet<>());
        try{
            service.save(entity);
            response.setSurvey(new SurveyDTO[]{map(entity)});
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
            try{
                service.save(updatedEntity);
                response.setSurvey(new SurveyDTO[]{map(updatedEntity)});
            }catch (Exception e){
                response.setMessage("Error updating survey");
            }
        }
        return response;
    }

    private SurveyResponseMessage handleVoteSurvey(long surveyId, String recipeUri, String userUri) {
        SurveyResponseMessage response = new SurveyResponseMessage();
        SurveyVote vote = new SurveyVote();
        vote.setRecipeUri(recipeUri);
        vote.setUserUri(userUri);
        Optional<SurveyEntity> entity = service.getSurveyById(surveyId);
        if (entity.isPresent()) {
            SurveyEntity updatedEntity = entity.get();
            updatedEntity.getVotes().add(vote);
            try{
                service.save(updatedEntity);
                response.setSurvey(new SurveyDTO[]{map(updatedEntity)});
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
         return new SurveyDTO(entity.getId(), entity.getTitle(), participants, entity.getOwnerUri(), recipeVotes, entity.getCreationDate());
    }

}
