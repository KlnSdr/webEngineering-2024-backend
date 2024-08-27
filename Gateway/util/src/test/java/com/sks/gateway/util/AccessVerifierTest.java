package com.sks.gateway.util;

import com.sks.users.api.UserDTO;
import com.sks.users.api.UsersRequestMessage;
import com.sks.users.api.UsersResponseMessage;
import com.sks.users.api.UsersSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AccessVerifierTest {
    @Mock
    private UsersSender usersSender;

    @Mock
    private Principal principal;

    @InjectMocks
    private AccessVerifier accessVerifier;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void verifyAccessesSelfReturnsTrueWhenUserIdsMatch() {
        long targetUserId = 1L;
        when(principal.getName()).thenReturn("1");
        UsersResponseMessage response = mock(UsersResponseMessage.class);
        UserDTO user = new UserDTO();
        user.setUserId(1L);
        when(usersSender.sendRequest(any(UsersRequestMessage.class))).thenReturn(response);
        when(response.getUser()).thenReturn(user);

        boolean result = accessVerifier.verifyAccessesSelf(targetUserId, principal);

        assertTrue(result);
    }

    @Test
    void verifyAccessesSelfReturnsFalseWhenUserIdsDoNotMatch() {
        long targetUserId = 1L;
        when(principal.getName()).thenReturn("2");
        UsersResponseMessage response = mock(UsersResponseMessage.class);
        UserDTO user = new UserDTO();
        user.setUserId(2L);
        when(usersSender.sendRequest(any(UsersRequestMessage.class))).thenReturn(response);
        when(response.getUser()).thenReturn(user);

        boolean result = accessVerifier.verifyAccessesSelf(targetUserId, principal);

        assertFalse(result);
    }

    @Test
    void verifyAccessesSelfReturnsFalseWhenPrincipalIsNull() {
        long targetUserId = 1L;

        boolean result = accessVerifier.verifyAccessesSelf(targetUserId, null);

        assertFalse(result);
    }

    @Test
    void verifyAccessesSelfReturnsFalseWhenUserNotFound() {
        long targetUserId = 1L;
        when(principal.getName()).thenReturn("1");
        UsersResponseMessage response = mock(UsersResponseMessage.class);
        when(usersSender.sendRequest(any(UsersRequestMessage.class))).thenReturn(response);
        when(response.getUser()).thenReturn(null);

        boolean result = accessVerifier.verifyAccessesSelf(targetUserId, principal);

        assertFalse(result);
    }
}
