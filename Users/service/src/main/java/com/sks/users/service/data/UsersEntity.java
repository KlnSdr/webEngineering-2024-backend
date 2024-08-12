package com.sks.users.service.data;

import jakarta.persistence.*;

@Entity
@Table(name = "users_entity_table")
public class UsersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(name = "idp_hash")
    private String idpHash;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdpHash() {
        return idpHash;
    }

    public void setIdpHash(String hash) {
        this.idpHash = idpHash;
    }
}