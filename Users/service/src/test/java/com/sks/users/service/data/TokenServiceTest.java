package com.sks.users.service.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TokenServiceTest {
    @Mock
    private TokenRepository tokenRepository;

    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        tokenRepository = mock(TokenRepository.class);
        tokenService = new TokenService(tokenRepository);
    }

    @Test
    void saveReturnsSavedTokenEntity() {
        TokenEntity tokenEntity = new TokenEntity();
        when(tokenRepository.save(tokenEntity)).thenReturn(tokenEntity);

        TokenEntity result = tokenService.save(tokenEntity);

        assertEquals(tokenEntity, result);
    }

    @Test
    void isKnownTokenReturnsTrueForExistingToken() {
        String token = "existingToken";
        when(tokenRepository.existsById(token)).thenReturn(true);

        boolean result = tokenService.isKnownToken(token);

        assertTrue(result);
    }

    @Test
    void isKnownTokenReturnsFalseForNonExistingToken() {
        String token = "nonExistingToken";
        when(tokenRepository.existsById(token)).thenReturn(false);

        boolean result = tokenService.isKnownToken(token);

        assertFalse(result);
    }

    @Test
    void deleteExpiredDeletesTokensBeforeCurrentDate() {
        Date currentDate = new Date();
        tokenService.deleteExpired();

        verify(tokenRepository, times(1)).deleteByValidTillBefore(currentDate);
    }
}
