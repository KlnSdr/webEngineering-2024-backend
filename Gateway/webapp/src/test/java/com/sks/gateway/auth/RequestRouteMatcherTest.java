package com.sks.gateway.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RequestRouteMatcherTest {
    @Test
    void isRestrictedRouteReturnsTrueForGetRoute() {
        RequestRouteMatcher matcher = RequestRouteMatcher.builder().get("/api/resource/*").build();
        assertTrue(matcher.isRestrictedRoute("/api/resource/123", "GET"));
    }

    @Test
    void isRestrictedRouteReturnsFalseForNonMatchingGetRoute() {
        RequestRouteMatcher matcher = RequestRouteMatcher.builder().get("/api/resource/*").build();
        assertFalse(matcher.isRestrictedRoute("/api/other/123", "GET"));
    }

    @Test
    void isRestrictedRouteReturnsTrueForPostRoute() {
        RequestRouteMatcher matcher = RequestRouteMatcher.builder().post("/api/resource/*").build();
        assertTrue(matcher.isRestrictedRoute("/api/resource/123", "POST"));
    }

    @Test
    void isRestrictedRouteReturnsFalseForNonMatchingPostRoute() {
        RequestRouteMatcher matcher = RequestRouteMatcher.builder().post("/api/resource/*").build();
        assertFalse(matcher.isRestrictedRoute("/api/other/123", "POST"));
    }

    @Test
    void isRestrictedRouteReturnsTrueForPutRoute() {
        RequestRouteMatcher matcher = RequestRouteMatcher.builder().put("/api/resource/*").build();
        assertTrue(matcher.isRestrictedRoute("/api/resource/123", "PUT"));
    }

    @Test
    void isRestrictedRouteReturnsFalseForNonMatchingPutRoute() {
        RequestRouteMatcher matcher = RequestRouteMatcher.builder().put("/api/resource/*").build();
        assertFalse(matcher.isRestrictedRoute("/api/other/123", "PUT"));
    }

    @Test
    void isRestrictedRouteReturnsTrueForDeleteRoute() {
        RequestRouteMatcher matcher = RequestRouteMatcher.builder().delete("/api/resource/*").build();
        assertTrue(matcher.isRestrictedRoute("/api/resource/123", "DELETE"));
    }

    @Test
    void isRestrictedRouteReturnsFalseForNonMatchingDeleteRoute() {
        RequestRouteMatcher matcher = RequestRouteMatcher.builder().delete("/api/resource/*").build();
        assertFalse(matcher.isRestrictedRoute("/api/other/123", "DELETE"));
    }

    @Test
    void isRestrictedRouteReturnsFalseForUnsupportedMethod() {
        RequestRouteMatcher matcher = RequestRouteMatcher.builder().get("/api/resource/*").build();
        assertFalse(matcher.isRestrictedRoute("/api/resource/123", "PATCH"));
    }
}
