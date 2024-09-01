package com.sks.gateway.surveys.rest;

<<<<<<< HEAD
import com.sks.gateway.util.AccessVerifier;
=======
import com.sks.gateway.surveys.dto.MySurveysDTO;
import com.sks.gateway.util.UserHelper;
>>>>>>> db9997e ([BACKEND-36]: change endpoint /surveys/user/<id> to /surveys/my to return all owned and participating surveys)
import com.sks.surveys.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/surveys")
public class SurveysResource {

    private final SurveySender surveySender;
    private final UserHelper userHelper;

    public SurveysResource(SurveySender surveySender , UserHelper userHelper) {
        this.surveySender = surveySender;
        this.userHelper = userHelper;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public SurveyDTO getSurveyById(@PathVariable("id") Integer id, Principal principal) {
        final UserDTO user = userHelper.getCurrentInternalUser(principal);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        final SurveyResponseMessage response = surveySender.sendRequest(new SurveyRequestMessage(id, SurveyRequestType.GET_SurveyById));
        if(response.getSurveys().length == 0){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Survey not found");
        }

        List<String> participants = List.of(response.getSurveys()[0].getParticipants());
        if (!participants.contains("/users/id/" + user.getUserId()) && !Objects.equals(response.getSurveys()[0].getCreator(), "/users/id/" + user.getUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        return response.getSurveys()[0];

    }

    @GetMapping("/my")
    @ResponseBody
    public MySurveysDTO getSurveysByUserId(Principal principal) {
        final UserDTO user = userHelper.getCurrentInternalUser(principal);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        final SurveyResponseMessage response = surveySender.sendRequest(new SurveyRequestMessage("/users/id/" + user.getUserId(), SurveyRequestType.GET_SurveyByOwner));
        final SurveyDTO[] ownedSurveys = response.getSurveys();

        final SurveyResponseMessage responseParticipating = surveySender.sendRequest(SurveyRequestMessage.getParticipating("/users/id/" + user.getUserId()));
        final SurveyDTO[] participatingSurveys = responseParticipating.getSurveys();

        return new MySurveysDTO(ownedSurveys, participatingSurveys);
    };

    @PostMapping
    @ResponseBody
    public SurveyDTO createSurvey(@RequestBody SurveyDTO survey, Principal principal) {
        final UserDTO user = userHelper.getCurrentInternalUser(principal);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        if (!isSurveyValid(survey)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Survey is not valid");
        }
        survey.setCreator("/users/id/" + user.getUserId());
        final SurveyResponseMessage response = surveySender.sendRequest(new SurveyRequestMessage(survey, SurveyRequestType.POST_SaveSurvey));
        if(response.getSurveys().length == 0){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create survey");
        }
        return response.getSurveys()[0];
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteSurvey(@PathVariable("id") int id, Principal principal) {
        final UserDTO user = userHelper.getCurrentInternalUser(principal);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        final SurveyResponseMessage responseGet = surveySender.sendRequest(new SurveyRequestMessage(id, SurveyRequestType.GET_SurveyById));
        if(responseGet.getSurveys().length == 0){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Survey not found");
        }

        if (!Objects.equals(responseGet.getSurveys()[0].getCreator(), "/users/id/" + user.getUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        final SurveyResponseMessage response = surveySender.sendRequest(new SurveyRequestMessage(id, SurveyRequestType.DELETE_DeleteSurvey));
        if (response.getMessage() != null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, response.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public SurveyDTO updateSurvey(@PathVariable("id") int id, @RequestBody SurveyDTO survey, Principal principal) {
        final UserDTO user = userHelper.getCurrentInternalUser(principal);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        if (survey.getId() != id || !isSurveyValid(survey)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Survey is not valid");
        }

        final SurveyResponseMessage responseGet = surveySender.sendRequest(new SurveyRequestMessage(id, SurveyRequestType.GET_SurveyById));

        if(responseGet.getSurveys().length == 0){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Survey not found");
        }

        if (!Objects.equals(responseGet.getSurveys()[0].getCreator(), "/users/id/" + user.getUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        final SurveyResponseMessage response = surveySender.sendRequest(new SurveyRequestMessage(survey, SurveyRequestType.PUT_UpdateSurvey));
        if(response.getSurveys().length == 0){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update survey");
        }
        return response.getSurveys()[0];
    }

    @PutMapping("/{id}/vote/{recipeId}")
    @ResponseBody
    public SurveyDTO voteForRecipe(@PathVariable("id") int surveyId, @PathVariable("recipeId") int recipeId, Principal principal) {
        final UserDTO user = userHelper.getCurrentInternalUser(principal);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        final SurveyResponseMessage responseGet = surveySender.sendRequest(new SurveyRequestMessage(surveyId, SurveyRequestType.GET_SurveyById));
        if(responseGet.getSurveys().length == 0){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Survey not found");
        }

        List<String> participants = List.of(responseGet.getSurveys()[0].getParticipants());
        if (!participants.contains("/users/id/" + user.getUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        final SurveyResponseMessage response = surveySender.sendRequest(new SurveyRequestMessage("/recipes/" + recipeId, "/users/id/"+ user.getUserId(), surveyId, SurveyRequestType.PUT_VoteSurvey));
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
        if (survey.getOptions() == null || survey.getOptions().isEmpty()) {
            return false;
        }
        return true;

    }



}
