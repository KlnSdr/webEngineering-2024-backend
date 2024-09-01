package com.sks.gateway.surveys.rest;

import com.sks.gateway.util.AccessVerifier;
import com.sks.surveys.api.*;
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

@RestController
@RequestMapping("/surveys")
public class SurveysResource {

    private final SurveySender surveySender;
    private final AccessVerifier accessVerifier;

    public SurveysResource(SurveySender surveySender, AccessVerifier accessVerifier) {
        this.surveySender = surveySender;
        this.accessVerifier = accessVerifier;
    }

    @Operation(summary = "Get survey by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the survey", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = SurveyDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Survey not found", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content)
    })
    @GetMapping("/{id}/user/{userId}")
    @ResponseBody
    public SurveyDTO getSurveyById(
            @Parameter(description = "ID of the survey to be fetched") @PathVariable("id") Integer id,
            @Parameter(description = "ID of the user") @PathVariable("userId") long userId) {
        final SurveyResponseMessage response = surveySender.sendRequest(new SurveyRequestMessage(id, SurveyRequestType.GET_SurveyById));
        if (response.getSurveys().length == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Survey not found");
        }

        List<String> participants = List.of(response.getSurveys()[0].getParticipants());
        if (!participants.contains("/users/id/" + userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        return response.getSurveys()[0];
    }

    @Operation(summary = "Get surveys by user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the surveys", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SurveyDTO.class)))}),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content),
            @ApiResponse(responseCode = "404", description = "No surveys found", content = @Content)
    })
    @GetMapping("/user/{userId}")
    @ResponseBody
    public SurveyDTO[] getSurveysByUserId(
            @Parameter(description = "ID of the user") @PathVariable("userId") int userId,
            Principal principal) {
        if (!accessVerifier.verifyAccessesSelf(userId, principal)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        final SurveyResponseMessage response = surveySender.sendRequest(new SurveyRequestMessage("/users/id/" + userId, SurveyRequestType.GET_SurveyByOwner));
        if (response.getSurveys().length == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No surveys found");
        }

        return response.getSurveys();
    }

    @Operation(summary = "Create a new survey")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Survey created", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = SurveyDTO.class))}),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content),
            @ApiResponse(responseCode = "400", description = "Survey is not valid", content = @Content),
            @ApiResponse(responseCode = "500", description = "Failed to create survey", content = @Content)
    })
    @PostMapping("/user/{userId}")
    @ResponseBody
    public SurveyDTO createSurvey(
            @Parameter(description = "Survey to be created") @RequestBody SurveyDTO survey,
            @Parameter(description = "ID of the user") @PathVariable("userId") long userId,
            Principal principal) {
        if (!accessVerifier.verifyAccessesSelf(userId, principal)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        if (!isSurveyValid(survey)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Survey is not valid");
        }
        final SurveyResponseMessage response = surveySender.sendRequest(new SurveyRequestMessage(survey, SurveyRequestType.POST_SaveSurvey));
        if (response.getSurveys().length == 0) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create survey");
        }
        return response.getSurveys()[0];
    }

    @Operation(summary = "Delete a survey")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Survey deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "Survey not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteSurvey(
            @Parameter(description = "ID of the survey to be deleted") @PathVariable("id") int id) {
        final SurveyResponseMessage response = surveySender.sendRequest(new SurveyRequestMessage(id, SurveyRequestType.DELETE_DeleteSurvey));
        if (response.getMessage() != null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, response.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Update a survey")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Survey updated", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = SurveyDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Survey is not valid", content = @Content),
            @ApiResponse(responseCode = "500", description = "Failed to update survey", content = @Content)
    })
    @PutMapping("/{id}")
    @ResponseBody
    public SurveyDTO updateSurvey(
            @Parameter(description = "ID of the survey to be updated") @PathVariable("id") int id,
            @Parameter(description = "Updated survey data") @RequestBody SurveyDTO survey) {
        if (survey.getId() != id || !isSurveyValid(survey)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Survey is not valid");
        }
        final SurveyResponseMessage response = surveySender.sendRequest(new SurveyRequestMessage(survey, SurveyRequestType.PUT_UpdateSurvey));
        if (response.getSurveys().length == 0) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update survey");
        }
        return response.getSurveys()[0];
    }

    @Operation(summary = "Vote for a recipe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vote successful", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = SurveyDTO.class))}),
            @ApiResponse(responseCode = "500", description = "Failed to vote for recipe", content = @Content)
    })
    @PutMapping("/{id}/vote/{recipeId}/{userId}")
    @ResponseBody
    public SurveyDTO voteForRecipe(
            @Parameter(description = "ID of the survey") @PathVariable("id") int surveyId,
            @Parameter(description = "ID of the recipe") @PathVariable("recipeId") int recipeId,
            @Parameter(description = "ID of the user") @PathVariable("userId") int userId) {
        final SurveyResponseMessage response = surveySender.sendRequest(new SurveyRequestMessage("/recipes/" + recipeId, "/users/id/" + userId, surveyId, SurveyRequestType.PUT_VoteSurvey));
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
        if (survey.getCreator() == null) {
            return false;
        }
        if (survey.getOptions() == null || survey.getOptions().isEmpty()) {
            return false;
        }
        return true;
    }
}