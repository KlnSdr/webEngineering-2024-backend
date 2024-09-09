package com.sks.gateway.auth;

import com.sks.gateway.RestrictedRoutesConfig;
import com.sks.users.api.UsersRequestMessage;
import com.sks.users.api.UsersResponseMessage;
import com.sks.users.api.UsersSender;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter that handles JWT authentication for incoming HTTP requests.
 * This filter checks the validity of the JWT token and sets the authentication
 * in the security context if the token is valid.
 */
@Component
public class JwtAuthPreFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final RequestRouteMatcher requestRouteMatcher;
    private final UsersSender usersSender;

    /**
     * Constructs a new JwtAuthPreFilter with the specified dependencies.
     *
     * @param jwtUtil the utility class for handling JWT operations
     * @param restrictedRoutesConfig the configuration for restricted routes
     * @param usersSender the sender for user-related messages
     */
    public JwtAuthPreFilter(JwtUtil jwtUtil, RestrictedRoutesConfig restrictedRoutesConfig, UsersSender usersSender) {
        this.jwtUtil = jwtUtil;
        this.requestRouteMatcher = restrictedRoutesConfig.getRestrictedRoutes();
        this.usersSender = usersSender;
    }

    /**
     * Filters incoming requests and performs JWT authentication.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @param filterChain the filter chain
     * @throws ServletException if an error occurs during filtering
     * @throws IOException if an I/O error occurs during filtering
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String path = request.getRequestURI();
        final String method = request.getMethod();
        final boolean authRequired = requestRouteMatcher.isRestrictedRoute(path, method);

        final String authHeader = request.getHeader("Authorization");
        final String token;
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            if (!authRequired) {
                filterChain.doFilter(request, response);
                return;
            }
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized");
            return;
        }

        token = authHeader.substring(7);

        if (!jwtUtil.isValidToken(token)) {
            if (!authRequired) {
                filterChain.doFilter(request, response);
                return;
            }
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized");
            return;
        }

        final UsersResponseMessage tokenResponse = usersSender.sendRequest(UsersRequestMessage.isKnownToken(token));

        if (tokenResponse.didError()) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Internal Server Error");
            return;
        }

        if (!tokenResponse.isKnownToken()) {
            if (!authRequired) {
                filterChain.doFilter(request, response);
                return;
            }
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized");
            return;
        }

        username = jwtUtil.extractSubject(token);

        if (username == null || SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        final UserDetails userDetails = User.builder()
                .username(username)
                .password("")
                .build();

        if (!jwtUtil.validateToken(token, userDetails)) {
            if (!authRequired) {
                filterChain.doFilter(request, response);
                return;
            }
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized");
            return;
        }

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}