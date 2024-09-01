package com.sks.gateway.surveys.rest;

import com.sks.gateway.common.MessageErrorHandler;
import com.sks.gateway.surveys.dto.MySurveysDTO;
import com.sks.gateway.util.UserHelper;
import com.sks.surveys.api.*;
import com.sks.users.api.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/surveys")
public class SurveysResource {

    private final SurveySender surveySender;
    private final UserHelper userHelper;
    private final MessageErrorHandler messageErrorHandler;

    public SurveysResource(SurveySender surveySender, UserHelper userHelper, MessageErrorHandler messageErrorHandler) {
        this.surveySender = surveySender;
        this.userHelper = userHelper;
        this.messageErrorHandler = messageErrorHandler;
    }

    @Operation(summary = "Get survey by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the survey", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = SurveyDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Survey not found", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "500", description = "Failed to send/receive message to/from service", content = @Content)
    })
    @GetMapping("/{id}")
    @ResponseBody
    public SurveyDTO getSurveyById(
            @Parameter(description = "ID of the survey to be fetched") @PathVariable("id") Integer id,
            Principal principal) {
        final UserDTO user = userHelper.getCurrentInternalUser(principal);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        final SurveyResponseMessage response = surveySender.sendRequest(new SurveyRequestMessage(id, SurveyRequestType.GET_SurveyById));

        if (response.didError()) {
            messageErrorHandler.handle(response);
        }

        if (response.getSurveys().length == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Survey not found");
        }

        List<String> participants = List.of(response.getSurveys()[0].getParticipants());
        if (!participants.contains("/users/id/" + user.getUserId()) && !Objects.equals(response.getSurveys()[0].getCreator(), "/users/id/" + user.getUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        return response.getSurveys()[0];
    }

    @Operation(summary = "Get all surveys a user is either the creator of or participating in")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the surveys", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MySurveysDTO.class)))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "500", description = "Failed to send/receive message to/from service", content = @Content)
    })
    @GetMapping("/my")
    @ResponseBody
    public MySurveysDTO getSurveysByUserId(Principal principal) {
        final UserDTO user = userHelper.getCurrentInternalUser(principal);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        final SurveyResponseMessage response = surveySender.sendRequest(new SurveyRequestMessage("/users/id/" + user.getUserId(), SurveyRequestType.GET_SurveyByOwner));
        final SurveyDTO[] ownedSurveys = response.getSurveys();

        if (response.didError()) {
            messageErrorHandler.handle(response);
        }


        final SurveyResponseMessage responseParticipating = surveySender.sendRequest(SurveyRequestMessage.getParticipating("/users/id/" + user.getUserId()));
        final SurveyDTO[] participatingSurveys = responseParticipating.getSurveys();

        if (responseParticipating.didError()) {
            messageErrorHandler.handle(responseParticipating);
        }


        return new MySurveysDTO(ownedSurveys, participatingSurveys);
    }

    @Operation(summary = "Create a new survey")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Survey created", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = SurveyDTO.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "400", description = "Survey is not valid", content = @Content),
            @ApiResponse(responseCode = "500", description = "Failed to create survey", content = @Content),
            @ApiResponse(responseCode = "500", description = "Failed to send/receive message to/from service", content = @Content)
    })
    @PostMapping
    @ResponseBody
    public SurveyDTO createSurvey(
            @Parameter(description = "Survey to be created") @RequestBody SurveyDTO survey,
            Principal principal) {
        final UserDTO user = userHelper.getCurrentInternalUser(principal);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        if (!isSurveyValid(survey)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Survey is not valid");
        }
        survey.setCreator("/users/id/" + user.getUserId());
        final SurveyResponseMessage response = surveySender.sendRequest(new SurveyRequestMessage(survey, SurveyRequestType.POST_SaveSurvey));

        if (response.didError()) {
            messageErrorHandler.handle(response);
        }

        if (response.getSurveys().length == 0) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create survey");
        }
        return response.getSurveys()[0];
    }

    @Operation(summary = "Delete a survey")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Survey deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "Survey not found", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "500", description = "Failed to send/receive message to/from service", content = @Content)
    })
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteSurvey(
            @Parameter(description = "ID of the survey to be deleted") @PathVariable("id") int id,
            Principal principal) {
        final UserDTO user = userHelper.getCurrentInternalUser(principal);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        final SurveyResponseMessage responseGet = surveySender.sendRequest(new SurveyRequestMessage(id, SurveyRequestType.GET_SurveyById));
        if (responseGet.getSurveys().length == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Survey not found");
        }

        if (!Objects.equals(responseGet.getSurveys()[0].getCreator(), "/users/id/" + user.getUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        final SurveyResponseMessage response = surveySender.sendRequest(new SurveyRequestMessage(id, SurveyRequestType.DELETE_DeleteSurvey));

        if (response.didError()) {
            messageErrorHandler.handle(response);
        }

        if (response.getMessage() != null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, response.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Update a survey")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Survey updated", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = SurveyDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Survey is not valid", content = @Content),
            @ApiResponse(responseCode = "404", description = "Survey not found", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "500", description = "Failed to update survey", content = @Content),
            @ApiResponse(responseCode = "500", description = "Failed to send/receive message to/from service", content = @Content)
    })
    @PutMapping("/{id}")
    @ResponseBody
    public SurveyDTO updateSurvey(
            @Parameter(description = "ID of the survey to be updated") @PathVariable("id") int id,
            @Parameter(description = "Updated survey data") @RequestBody SurveyDTO survey,
            Principal principal) {
        final UserDTO user = userHelper.getCurrentInternalUser(principal);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        if (survey.getId() != id || !isSurveyValid(survey)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Survey is not valid");
        }

        final SurveyResponseMessage responseGet = surveySender.sendRequest(new SurveyRequestMessage(id, SurveyRequestType.GET_SurveyById));

        if (responseGet.getSurveys().length == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Survey not found");
        }

        if (!Objects.equals(responseGet.getSurveys()[0].getCreator(), "/users/id/" + user.getUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        final SurveyResponseMessage response = surveySender.sendRequest(new SurveyRequestMessage(survey, SurveyRequestType.PUT_UpdateSurvey));

        if (response.didError()) {
            messageErrorHandler.handle(response);
        }

        if (response.getSurveys().length == 0) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update survey");
        }
        return response.getSurveys()[0];
    }

    @Operation(summary = "Vote for a recipe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vote successful", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = SurveyDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Survey not found", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "500", description = "Failed to vote for recipe", content = @Content),
            @ApiResponse(responseCode = "500", description = "Failed to send/receive message to/from service", content = @Content)
    })
    @PutMapping("/{id}/vote/{recipeId}")
    @ResponseBody
    public SurveyDTO voteForRecipe(
            @Parameter(description = "ID of the survey") @PathVariable("id") int surveyId,
            @Parameter(description = "ID of the recipe") @PathVariable("recipeId") int recipeId,
            Principal principal) {
        final UserDTO user = userHelper.getCurrentInternalUser(principal);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        final SurveyResponseMessage responseGet = surveySender.sendRequest(new SurveyRequestMessage(surveyId, SurveyRequestType.GET_SurveyById));
        if (responseGet.getSurveys().length == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Survey not found");
        }

        List<String> participants = List.of(responseGet.getSurveys()[0].getParticipants());
        if (!participants.contains("/users/id/" + user.getUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        final SurveyResponseMessage response = surveySender.sendRequest(new SurveyRequestMessage("/recipes/" + recipeId, "/users/id/" + user.getUserId(), surveyId, SurveyRequestType.PUT_VoteSurvey));

        if (response.didError()) {
            messageErrorHandler.handle(response);
        }
        if (response.getMessage() != null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to vote for recipe");
        }
        return response.getSurveys()[0];
    }

    private boolean isSurveyValid(SurveyDTO survey) {
        if (survey.getTitle().isEmpty()) {
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