package com.sks.surveys.service.data;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "surveys_entity_table")
public class SurveyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "surveys_id")
    private Long id;

    @Column(name = "owner_uri")
    private String ownerUri;

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SurveyVote> votes = new HashSet<>();

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

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}