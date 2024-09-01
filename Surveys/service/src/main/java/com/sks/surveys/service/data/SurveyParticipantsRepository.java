package com.sks.surveys.service.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface SurveyParticipantsRepository extends JpaRepository<SurveyParticipants, Long> {
    Set<SurveyParticipants> findByUserUri(String userUri);
}
