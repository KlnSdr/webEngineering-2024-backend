package com.sks.users.service.data;

import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {
    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public TokenEntity save(TokenEntity tokenEntity) {
        return tokenRepository.save(tokenEntity);
    }

    public boolean isKnownToken(String token) {
        return tokenRepository.existsById(token);
    }

    public void deleteExpired() {
        tokenRepository.deleteByValidTillBefore(new Date());
    }
}
