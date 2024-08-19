package com.sks.gateway.users;

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
import java.util.Objects;

@Component
public class OAuthHandler implements AuthenticationSuccessHandler {
    @Value("${app.oauth2.successRedirectUrl}")
    private String redirectUrl;

    private final UsersSender usersSender;

    public OAuthHandler(UsersSender usersSender) {
        this.usersSender = usersSender;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println((Integer) oAuth2User.getAttribute("id"));
        System.out.println((String) oAuth2User.getAttribute("username"));

        final UsersResponseMessage userResponse = usersSender.sendRequest(UsersRequestMessage.createUser(
                (String) oAuth2User.getAttribute("username"),
                ((Integer) Objects.requireNonNull(oAuth2User.getAttribute("id"))).longValue()
        ));

        // Redirect to the desired URL after login
        response.sendRedirect(redirectUrl);  // Adjust the redirect URL as needed
    }
}
