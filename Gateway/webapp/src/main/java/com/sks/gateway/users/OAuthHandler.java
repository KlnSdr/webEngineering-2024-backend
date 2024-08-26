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
    private final UsersSender usersSender;
    @Value("${app.oauth2.successRedirectUrl}")
    private String redirectUrl;

    public OAuthHandler(UsersSender usersSender) {
        this.usersSender = usersSender;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        try {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            final UsersResponseMessage _userResponse = usersSender.sendRequest(UsersRequestMessage.createUser(
                    oAuth2User.getAttribute("username"),
                    ((Integer) Objects.requireNonNull(oAuth2User.getAttribute("id"))).longValue()
            ));

            // Redirect to the desired URL after login
            response.sendRedirect(redirectUrl);  // Adjust the redirect URL as needed
        } catch (Exception e) {
            response.sendRedirect("/login?error=true");  // Adjust the redirect URL as needed
        }
    }
}
