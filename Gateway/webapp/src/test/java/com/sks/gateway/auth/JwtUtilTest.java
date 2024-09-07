package com.sks.gateway.auth;

import com.sks.users.api.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JwtUtilTest {
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
    }

    @Test
    void generateTokenReturnsValidToken() {
        OAuth2User oAuth2User = mock(OAuth2User.class);
        UserDTO internalUser = new UserDTO();
        internalUser.setUserId(1L);
        when(oAuth2User.getName()).thenReturn("testUser");
        when(oAuth2User.getAttribute("username")).thenReturn("testUsername");
        when(oAuth2User.getAttribute("avatar_url")).thenReturn("testAvatarUrl");

        String token = jwtUtil.generateToken(oAuth2User, internalUser);

        assertNotNull(token);
        assertTrue(jwtUtil.isValidToken(token));
    }

    @Test
    void isValidTokenReturnsFalseForInvalidToken() {
        String invalidToken = "invalidToken";

        boolean result = jwtUtil.isValidToken(invalidToken);

        assertFalse(result);
    }

    @Test
    void extractUsernameReturnsCorrectUsername() {
        OAuth2User oAuth2User = mock(OAuth2User.class);
        UserDTO internalUser = new UserDTO();
        internalUser.setUserId(1L);
        when(oAuth2User.getName()).thenReturn("testUser");
        when(oAuth2User.getAttribute("username")).thenReturn("testUsername");
        when(oAuth2User.getAttribute("avatar_url")).thenReturn("testAvatarUrl");

        String token = jwtUtil.generateToken(oAuth2User, internalUser);
        String username = jwtUtil.extractUsername(token);

        assertEquals("testUsername", username);
    }

    @Test
    void extractSubjectReturnsCorrectSubject() {
        OAuth2User oAuth2User = mock(OAuth2User.class);
        UserDTO internalUser = new UserDTO();
        internalUser.setUserId(1L);
        when(oAuth2User.getName()).thenReturn("testUser");
        when(oAuth2User.getAttribute("username")).thenReturn("testUsername");
        when(oAuth2User.getAttribute("avatar_url")).thenReturn("testAvatarUrl");

        String token = jwtUtil.generateToken(oAuth2User, internalUser);
        String subject = jwtUtil.extractSubject(token);

        assertEquals("testUser", subject);
    }

    @Test
    void validateTokenReturnsTrueForValidToken() {
        OAuth2User oAuth2User = mock(OAuth2User.class);
        UserDTO internalUser = new UserDTO();
        internalUser.setUserId(1L);
        UserDetails userDetails = mock(UserDetails.class);
        when(oAuth2User.getName()).thenReturn("testUser");
        when(oAuth2User.getAttribute("username")).thenReturn("testUsername");
        when(oAuth2User.getAttribute("avatar_url")).thenReturn("testAvatarUrl");
        when(userDetails.getUsername()).thenReturn("testUser");

        String token = jwtUtil.generateToken(oAuth2User, internalUser);
        boolean result = jwtUtil.validateToken(token, userDetails);

        assertTrue(result);
    }

    @Test
    void validateTokenReturnsFalseForExpiredToken() {
        OAuth2User oAuth2User = mock(OAuth2User.class);
        UserDTO internalUser = new UserDTO();
        internalUser.setUserId(1L);
        UserDetails userDetails = mock(UserDetails.class);
        when(oAuth2User.getName()).thenReturn("testUser");
        when(oAuth2User.getAttribute("username")).thenReturn("testUsername");
        when(oAuth2User.getAttribute("avatar_url")).thenReturn("testAvatarUrl");
        when(userDetails.getUsername()).thenReturn("testUser");

        JwtUtil jwtUtilWithShortExpiry = new JwtUtil() {
            {
                jwtTokenValidity = 1; // 1 millisecond for quick expiry
            }
        };

        String token = jwtUtilWithShortExpiry.generateToken(oAuth2User, internalUser);
        try {
            Thread.sleep(2); // Ensure the token is expired
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertThrows(RuntimeException.class, () -> jwtUtilWithShortExpiry.validateToken(token, userDetails));
    }
}
