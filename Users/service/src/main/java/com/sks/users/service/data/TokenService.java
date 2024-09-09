package com.sks.users.service.data;

import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Service class for managing tokens.
 * Provides methods to save tokens, check if a token is known, and delete expired tokens.
 */
@Service
public class TokenService {
    private final TokenRepository tokenRepository;

    /**
     * Constructs a new TokenService with the specified token repository.
     *
     * @param tokenRepository the token repository
     */
    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    /**
     * Saves the given token entity to the repository.
     *
     * @param tokenEntity the token entity to save
     * @return the saved token entity
     */
    public TokenEntity save(TokenEntity tokenEntity) {
        return tokenRepository.save(tokenEntity);
    }

    /**
     * Checks if a token is known by verifying its existence in the repository.
     *
     * @param token the token to check
     * @return true if the token is known, false otherwise
     */
    public boolean isKnownToken(String token) {
        return tokenRepository.existsById(token);
    }

    /**
     * Deletes all tokens that have expired.
     */
    public void deleteExpired() {
        tokenRepository.deleteByValidTillBefore(new Date());
    }
}