package com.sks.surveys.service.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SurveyRepository extends JpaRepository<SurveyEntity, Long> {
    List<SurveyEntity> findByOwnerUri(String ownerUri);

}
