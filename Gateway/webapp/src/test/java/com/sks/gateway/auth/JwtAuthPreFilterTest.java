package com.sks.gateway.auth;

import com.sks.gateway.RestrictedRoutesConfig;
import com.sks.users.api.UsersRequestMessage;
import com.sks.users.api.UsersResponseMessage;
import com.sks.users.api.UsersSender;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class JwtAuthPreFilterTest {
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private RestrictedRoutesConfig restrictedRoutesConfig;
    @Mock
    private UsersSender usersSender;

    private JwtAuthPreFilter jwtAuthPreFilter;

    @BeforeEach
    void setUp() {
        jwtUtil = mock(JwtUtil.class);
        restrictedRoutesConfig = mock(RestrictedRoutesConfig.class);
        usersSender = mock(UsersSender.class);
        when(restrictedRoutesConfig.getRestrictedRoutes()).thenReturn(RequestRouteMatcher.builder().get("/api/resource").build());
        jwtAuthPreFilter = new JwtAuthPreFilter(jwtUtil, restrictedRoutesConfig, usersSender);
    }

    @Test
    void doFilterInternalAllowsRequestWhenRouteIsNotRestricted() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getRequestURI()).thenReturn("/public/resource");
        when(request.getMethod()).thenReturn("GET");

        jwtAuthPreFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternalReturnsUnauthorizedWhenNoAuthHeader() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getRequestURI()).thenReturn("/api/resource");
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Authorization")).thenReturn(null);
        when(response.getWriter()).thenReturn(mock(java.io.PrintWriter.class));

        jwtAuthPreFilter.doFilterInternal(request, response, filterChain);

        verify(response, times(1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response.getWriter(), times(1)).write("Unauthorized");
    }

    @Test
    void doFilterInternalReturnsUnauthorizedWhenInvalidToken() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getRequestURI()).thenReturn("/api/resource");
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Authorization")).thenReturn("Bearer invalidToken");
        when(response.getWriter()).thenReturn(mock(java.io.PrintWriter.class));
        when(jwtUtil.isValidToken("invalidToken")).thenReturn(false);

        jwtAuthPreFilter.doFilterInternal(request, response, filterChain);

        verify(response, times(1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response.getWriter(), times(1)).write("Unauthorized");
    }

    @Test
    void doFilterInternalReturnsInternalServerErrorWhenTokenResponseError() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        UsersResponseMessage tokenResponse = mock(UsersResponseMessage.class);
        when(tokenResponse.didError()).thenReturn(true);

        when(request.getRequestURI()).thenReturn("/api/resource");
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        when(response.getWriter()).thenReturn(mock(java.io.PrintWriter.class));
        when(jwtUtil.isValidToken("validToken")).thenReturn(true);
        when(usersSender.sendRequest(any(UsersRequestMessage.class))).thenReturn(tokenResponse);

        jwtAuthPreFilter.doFilterInternal(request, response, filterChain);

        verify(response, times(1)).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        verify(response.getWriter(), times(1)).write("Internal Server Error");
    }

    @Test
    void doFilterInternalReturnsUnauthorizedWhenTokenNotKnown() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getRequestURI()).thenReturn("/api/resource");
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        when(response.getWriter()).thenReturn(mock(java.io.PrintWriter.class));
        when(jwtUtil.isValidToken("validToken")).thenReturn(true);
        UsersResponseMessage tokenResponse = mock(UsersResponseMessage.class);
        when(tokenResponse.didError()).thenReturn(false);
        when(tokenResponse.isKnownToken()).thenReturn(false);
        when(usersSender.sendRequest(any(UsersRequestMessage.class))).thenReturn(tokenResponse);

        jwtAuthPreFilter.doFilterInternal(request, response, filterChain);

        verify(response, times(1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response.getWriter(), times(1)).write("Unauthorized");
    }

    @Test
    void doFilterInternalSetsAuthenticationWhenTokenIsValid() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getRequestURI()).thenReturn("/api/resource");
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        when(response.getWriter()).thenReturn(mock(java.io.PrintWriter.class));
        when(jwtUtil.isValidToken("validToken")).thenReturn(true);
        UsersResponseMessage tokenResponse = mock(UsersResponseMessage.class);
        when(tokenResponse.didError()).thenReturn(false);
        when(tokenResponse.isKnownToken()).thenReturn(true);
        when(usersSender.sendRequest(any(UsersRequestMessage.class))).thenReturn(tokenResponse);
        when(jwtUtil.extractSubject("validToken")).thenReturn("testUser");
        UserDetails userDetails = User.builder().username("testUser").password("").build();
        when(jwtUtil.validateToken("validToken", userDetails)).thenReturn(true);

        jwtAuthPreFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals("testUser", SecurityContextHolder.getContext().getAuthentication().getName());
        verify(filterChain, times(1)).doFilter(request, response);
    }
}
