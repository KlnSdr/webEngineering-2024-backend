package com.sks.users.service.data;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class TokenService {
    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public TokenEntity save(TokenEntity tokenEntity) {
        return tokenRepository.save(tokenEntity);
    }

    public Optional<TokenEntity> findById(String token) {
        return tokenRepository.findById(token);
    }

    public void deleteById(String token) {
        tokenRepository.deleteById(token);
    }

    public boolean isKnownToken(String token) {
        Optional<TokenEntity> tokenEntity = findById(token);

        if (tokenEntity.isEmpty()) {
            return false;
        }

        if (tokenEntity.get().getValidTill().before(new Date())) {
            deleteById(token);
            return false;
        }

        return true;
    }
}
