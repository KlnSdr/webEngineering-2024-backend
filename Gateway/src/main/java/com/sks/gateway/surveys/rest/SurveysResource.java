package com.sks.gateway.surveys.rest;

import com.sks.surveys.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/surveys")
public class SurveysResource {

    private final SurveySender surveySender;

    public SurveysResource(SurveySender surveySender) {
        this.surveySender = surveySender;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public SurveyDTO getSurveyById(@PathVariable("id") Integer id) {
        final SurveyResponseMessage response = surveySender.sendRequest(new SurveyRequestMessage(id, SurveyRequestType.GET_SurveyById));
        if(response.getSurveys().length == 0){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Survey not found");
        }

        return response.getSurveys()[0];

    }

    @GetMapping("user/{userId}")
    @ResponseBody
    public SurveyDTO[] getSurveysByUserId(@PathVariable("userId") int userId) {
        final SurveyResponseMessage response = surveySender.sendRequest(new SurveyRequestMessage("/users/id/" + userId, SurveyRequestType.GET_SurveyByOwner));
        if (response.getSurveys().length == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No surveys found");}

        return response.getSurveys();
        };

    @PostMapping
    @ResponseBody
    public SurveyDTO createSurvey(@RequestBody SurveyDTO survey) {
        if (!isSurveyValid(survey)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Survey is not valid");
        }
        final SurveyResponseMessage response = surveySender.sendRequest(new SurveyRequestMessage(survey, SurveyRequestType.POST_SaveSurvey));
        if(response.getSurveys().length == 0){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create survey");
        }
        return response.getSurveys()[0];
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteSurvey(@PathVariable("id") int id) {
        final SurveyResponseMessage response = surveySender.sendRequest(new SurveyRequestMessage(id, SurveyRequestType.DELETE_DeleteSurvey));
        if (response.getMessage() != null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, response.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public SurveyDTO updateSurvey(@PathVariable("id") int id, @RequestBody SurveyDTO survey) {
        if (survey.getId() != id || !isSurveyValid(survey)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Survey is not valid");
        }
        final SurveyResponseMessage response = surveySender.sendRequest(new SurveyRequestMessage(survey, SurveyRequestType.PUT_UpdateSurvey));
        if(response.getSurveys().length == 0){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update survey");
        }
        return response.getSurveys()[0];
    }

    @PutMapping("/{id}/vote/{recipeId}/{userId}")
    @ResponseBody
    public SurveyDTO voteForRecipe(@PathVariable("id") int surveyId, @PathVariable("recipeId") int recipeId, @PathVariable("userId") int userId) {

        final SurveyResponseMessage response = surveySender.sendRequest(new SurveyRequestMessage("/recipes/" + recipeId, "/users/"+ userId, surveyId, SurveyRequestType.PUT_VoteSurvey));
        if(response.getMessage() != null){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to vote for recipe");
        }
        return response.getSurveys()[0];
    }


    private boolean isSurveyValid(SurveyDTO survey) {
        if(survey.getTitle().isEmpty()){
            return false;
        }
        if (survey.getParticipants() == null) {
            return false;
        }
        if (survey.getCreator() == null) {
            return false;
        }
        if (survey.getOptions() == null || survey.getOptions().isEmpty()) {
            return false;
        }
        return true;

    }



}
