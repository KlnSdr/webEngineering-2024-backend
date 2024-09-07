package com.sks.users.service.data;

import org.springframework.stereotype.Service;

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
}
