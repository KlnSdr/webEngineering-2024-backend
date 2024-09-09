package com.sks.gateway.users;

import com.sks.gateway.auth.JwtUtil;
import com.sks.gateway.common.MessageErrorHandler;
import com.sks.users.api.UserDTO;
import com.sks.users.api.UsersRequestMessage;
import com.sks.users.api.UsersResponseMessage;
import com.sks.users.api.UsersSender;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;

/**
 * Handler for successful OAuth2 authentication.
 * This class implements the AuthenticationSuccessHandler interface and handles
 * the actions to be taken upon successful authentication.
 */
@Component
public class OAuthHandler implements AuthenticationSuccessHandler {
    private final UsersSender usersSender;
    private final MessageErrorHandler messageErrorHandler;
    private final JwtUtil jwtUtil;

    /**
     * The URL to redirect to after successful authentication.
     */
    @Value("${app.oauth2.successRedirectUrl}")
    private String redirectUrl;

    /**
     * Constructs an OAuthHandler with the specified UsersSender.
     *
     * @param usersSender the sender to use for sending user requests
     */
    public OAuthHandler(UsersSender usersSender, MessageErrorHandler messageErrorHandler, JwtUtil jwtUtil) {
        this.usersSender = usersSender;
        this.messageErrorHandler = messageErrorHandler;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Handles successful authentication by creating a new user in the system if it does not exist
     * and redirecting to the configured URL.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @param authentication the authentication object
     * @throws IOException if an input or output exception occurs
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        try {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            final UsersResponseMessage userResponse = usersSender.sendRequest(UsersRequestMessage.createUser(
                    oAuth2User.getAttribute("username"),
                    ((Integer) Objects.requireNonNull(oAuth2User.getAttribute("id"))).longValue()
            ));

            if (userResponse.didError() || userResponse.getUser() == null) {
                messageErrorHandler.handle(userResponse);
            }

            final UserDTO internalUser = userResponse.getUser();

            final String token = jwtUtil.generateToken(oAuth2User, internalUser);
            final Date validTill = jwtUtil.extractExpiration(token);

            final UsersResponseMessage tokenResponse = usersSender.sendRequest(UsersRequestMessage.storeToken(token, validTill));

            if (tokenResponse.didError()) {
                messageErrorHandler.handle(tokenResponse);
            }

            // Redirect to the desired URL after login
            response.sendRedirect(redirectUrl + "?token=" + token);
        } catch (Exception e) {
            response.sendRedirect("/login?error=true");
        }
    }
}