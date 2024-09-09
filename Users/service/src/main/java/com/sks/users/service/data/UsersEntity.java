package com.sks.users.service.data;

import jakarta.persistence.*;

/**
 * Entity class representing a user in the database.
 */
@Entity
@Table(name = "users_entity_table")
public class UsersEntity {

    /**
     * The unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    /**
     * The hash of the user's IDP (Identity Provider).
     */
    @Column(name = "idp_hash")
    private String idpHash;

    /**
     * The display name of the user.
     */
    @Column(name = "display_name")
    private String displayName;

    /**
     * Gets the display name of the user.
     *
     * @return the display name of the user
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets the display name of the user.
     *
     * @param displayName the display name to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the unique identifier of the user.
     *
     * @return the unique identifier of the user
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the user.
     *
     * @param id the unique identifier to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the hash of the user's IDP.
     *
     * @return the hash of the user's IDP
     */
    public String getIdpHash() {
        return idpHash;
    }

    /**
     * Sets the hash of the user's IDP.
     *
     * @param hash the hash to set
     */
    public void setIdpHash(String hash) {
        this.idpHash = hash;
    }
}