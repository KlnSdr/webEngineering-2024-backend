package com.sks.surveys.service.data;

import jakarta.persistence.*;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "surveys_entity_table")
public class SurveyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "surveys_id")
    private Long id;

    @Column(name = "survey_title")
    private String title;

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<SurveyParticipants> participants = new HashSet<>();

    @Column(name = "owner_uri")
    private String ownerUri;

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<SurveyVote> votes = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "survey_options", joinColumns = @JoinColumn(name = "survey_id"))
    private List<String> options;

    @Column(name = "Survey_Creation_Date", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date creationDate;

    public SurveyEntity() {
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public Set<SurveyVote> getVotes() {
        return votes;
    }

    public void setVotes(Set<SurveyVote> votes) {
        this.votes = votes;
    }

    public String getOwnerUri() {
        return ownerUri;
    }

    public void setOwnerUri(String ownerUri) {
        this.ownerUri = ownerUri;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Set<SurveyParticipants> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<SurveyParticipants> participants) {
        this.participants = participants;
    }
}