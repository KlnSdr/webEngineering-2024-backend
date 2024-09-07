package com.sks.users.service.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Date;

@Entity
@Table(name = "token_entity_table")
public class TokenEntity {
    @Id
    @Column(name = "token", columnDefinition = "VARCHAR(402)")
    private String token;
    @Column(name = "valid_till", columnDefinition = "TIMESTAMP")
    private Date validTill;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getValidTill() {
        return validTill;
    }

    public void setValidTill(Date validTill) {
        this.validTill = validTill;
    }
}
