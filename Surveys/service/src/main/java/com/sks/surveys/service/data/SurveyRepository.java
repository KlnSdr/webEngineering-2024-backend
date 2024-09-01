package com.sks.surveys.service.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SurveyRepository extends JpaRepository<SurveyEntity, Long> {
    Optional<SurveyEntity> findById(long id);
    List<SurveyEntity> findByOwnerUri(String ownerUri);

}
