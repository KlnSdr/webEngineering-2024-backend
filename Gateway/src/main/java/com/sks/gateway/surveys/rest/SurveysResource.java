package com.sks.gateway.surveys.rest;

import com.sks.recipes.api.dto.RecipeDTO;
import com.sks.surveys.api.SurveyDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.Map;


@RestController
@RequestMapping("/surveys")
public class SurveysResource {

    @GetMapping("/{id}")
    @ResponseBody
    public SurveyDTO getSurveyById(@PathVariable("id") Integer id) {
        return new SurveyDTO(
                id,
                "Survey",
                new String[]{"/users/41", "/users/40"},
                "/users/42",
                Map.of(new RecipeDTO(), 1),
                Date.from(Instant.now())
        );
    }

    @GetMapping("user/{userId}")
    @ResponseBody
    public SurveyDTO[] getSurveysByUserId(@PathVariable("userId") int userId) {
        return new SurveyDTO[]{
                new SurveyDTO(
                        1,
                        "Allgemeine Verkehrskontrolle"

                ),
                new SurveyDTO(
                        2,
                        "Wie zufrieden sind Sie mit der Mensa?"
                )
        };
    }

    @PostMapping
    @ResponseBody
    public SurveyDTO createSurvey(@RequestBody SurveyDTO survey) {
        final SurveyDTO mappedSurvey = map(survey);
        mappedSurvey.setId(42);
        mappedSurvey.setCreationDate(Date.from(Instant.now()));
        return mappedSurvey;
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteSurvey(@PathVariable("id") int id) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public SurveyDTO updateSurvey(@PathVariable("id") int id, @RequestBody SurveyDTO survey) {
        return survey;
    }

    @PutMapping("/{id}/vote/{recipeId}")
    @ResponseBody
    public SurveyDTO voteForRecipe(@PathVariable("id") int id, @PathVariable("recipeId") int recipeId) {
        return new SurveyDTO(
                id,
                "Survey",
                new String[]{"/users/41", "/users/40"},
                "/users/42",
                Map.of(new RecipeDTO(), 1),
                Date.from(Instant.now())
        );
    }

    private SurveyDTO map(SurveyDTO survey) {
        return new SurveyDTO(
                0,
                survey.getTitle(),
                survey.getParticipants(),
                survey.getCreator(),
                survey.getRecipeVote(),
                null
        );
    }


}
