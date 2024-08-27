package com.sks.surveys.service.data;

import jakarta.persistence.*;

@Entity
@Table(name = "survey_participants")
public class SurveyParticipants {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participant_id")
    private Long id;

    @Column(name = "user_uri")
    private String userUri;

    @ManyToOne
    @JoinColumn(name = "survey_id")
    private SurveyEntity survey;

    public SurveyParticipants() {

    }

    public SurveyParticipants(SurveyEntity entity, String participant) {
        this.survey = entity;
        this.userUri = participant;
    }



    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserUri() {
        return userUri;
    }

    public void setUserUri(String userUri) {
        this.userUri = userUri;
    }

    public SurveyEntity getSurvey() {
        return survey;
    }

    public void setSurvey(SurveyEntity survey) {
        this.survey = survey;
    }


}
