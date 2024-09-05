package com.sks.gateway.common;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.sks.users.api.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {
    private final String SECRET = "secret";
    private final long jwtTokenValidity = 5 * 60 * 60 * 1000;

    public String generateToken(OAuth2User oAuth2User, UserDTO internalUser) {
        return JWT.create()
                .withSubject(oAuth2User.getName())
                .withClaim("id", internalUser.getUserId())
                .withClaim("username", (String) oAuth2User.getAttribute("username"))
                .withClaim("profileImage", (String) oAuth2User.getAttribute("avatar_url"))
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtTokenValidity))
                .sign(Algorithm.HMAC256(SECRET));
    }

    public boolean isValidToken(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return extractClaim(token, jwt -> jwt.getClaim("username").asString());
    }

    public String extractSubject(String token) {
        return extractClaim(token, DecodedJWT::getSubject);
    }

    public <T> T extractClaim(String token, Function<DecodedJWT, T> claimsResolver) {
        final DecodedJWT decodedJWT = extractAllClaims(token);
        return claimsResolver.apply(decodedJWT);
    }

    private DecodedJWT extractAllClaims(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        try {
            return verifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, DecodedJWT::getExpiresAt);
    }
}
