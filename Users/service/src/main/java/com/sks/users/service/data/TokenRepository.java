package com.sks.users.service.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface TokenRepository extends JpaRepository<TokenEntity, String> {
    void deleteByValidTillBefore(Date now);
}
