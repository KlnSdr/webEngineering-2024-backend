package com.sks.surveys.service.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyRepository extends JpaRepository<SurveyEntity, Long> {
}
