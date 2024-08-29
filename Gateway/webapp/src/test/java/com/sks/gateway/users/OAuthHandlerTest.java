package com.sks.gateway.users;

import com.sks.gateway.common.MessageErrorHandler;
import com.sks.users.api.UsersRequestMessage;
import com.sks.users.api.UsersResponseMessage;
import com.sks.users.api.UsersSender;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class OAuthHandlerTest {

    @Mock
    private UsersSender usersSender;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    @Mock
    private OAuth2User oAuth2User;

    @Mock
    private MessageErrorHandler messageErrorHandler;

    @InjectMocks
    private OAuthHandler oAuthHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void onAuthenticationSuccessRedirectsToConfiguredUrl() throws IOException {
        when(authentication.getPrincipal()).thenReturn(oAuth2User);
        when(oAuth2User.getAttribute("username")).thenReturn("testUser");
        when(oAuth2User.getAttribute("id")).thenReturn(1);
        UsersResponseMessage userResponse = new UsersResponseMessage();
        when(usersSender.sendRequest(any(UsersRequestMessage.class))).thenReturn(userResponse);

        oAuthHandler.onAuthenticationSuccess(request, response, authentication);

        verify(response).sendRedirect(any());
    }

    @Test
    void onAuthenticationSuccessSendsUserRequest() throws IOException {
        when(authentication.getPrincipal()).thenReturn(oAuth2User);
        when(oAuth2User.getAttribute("username")).thenReturn("testUser");
        when(oAuth2User.getAttribute("id")).thenReturn(1);
        UsersResponseMessage userResponse = new UsersResponseMessage();
        when(usersSender.sendRequest(any(UsersRequestMessage.class))).thenReturn(userResponse);

        oAuthHandler.onAuthenticationSuccess(request, response, authentication);

        verify(usersSender).sendRequest(any(UsersRequestMessage.class));
    }

    @Test
    void onAuthenticationSuccessHandlesNullAttributesGracefully() throws IOException {
        when(authentication.getPrincipal()).thenReturn(oAuth2User);
        when(oAuth2User.getAttribute("username")).thenReturn(null);
        when(oAuth2User.getAttribute("id")).thenReturn(null);
        UsersResponseMessage userResponse = new UsersResponseMessage();
        when(usersSender.sendRequest(any(UsersRequestMessage.class))).thenReturn(userResponse);

        oAuthHandler.onAuthenticationSuccess(request, response, authentication);

        verify(response).sendRedirect(any());
    }

    @Test
    void onAuthenticationSuccess_Throws500OnMessageError() throws IOException {
        when(authentication.getPrincipal()).thenReturn(oAuth2User);
        when(oAuth2User.getAttribute("username")).thenReturn("testUser");
        when(oAuth2User.getAttribute("id")).thenReturn(1);
        UsersResponseMessage userResponse = new UsersResponseMessage();
        userResponse.setDidError(true);
        when(usersSender.sendRequest(any(UsersRequestMessage.class))).thenReturn(userResponse);

        oAuthHandler.onAuthenticationSuccess(request, response, authentication);

        verify(messageErrorHandler).handle(userResponse);
    }
}