package com.sks.gateway.surveys.dto;

import com.sks.surveys.api.SurveyDTO;

public record MySurveysDTO(SurveyDTO[] owned, SurveyDTO[] participating) {
}
