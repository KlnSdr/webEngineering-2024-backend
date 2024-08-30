package com.sks.gateway.util;

import com.sks.users.api.UserDTO;
import com.sks.users.api.UsersResponseMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AccessVerifierTest {
    @Mock
    private UserHelper userHelper;

    @Mock
    private Principal principal;

    private AccessVerifier accessVerifier;

    @BeforeEach
    void setUp() {
        userHelper = mock(UserHelper.class);
        principal = mock(Principal.class);
        accessVerifier = new AccessVerifier(userHelper);
    }

    @Test
    void verifyAccessesSelfReturnsTrueWhenUserIdsMatch() {
        long targetUserId = 1L;
        when(principal.getName()).thenReturn("1");
        UsersResponseMessage response = mock(UsersResponseMessage.class);
        UserDTO user = new UserDTO();
        user.setUserId(1L);
        when(userHelper.getCurrentInternalUser(any())).thenReturn(user);
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
        when(userHelper.getCurrentInternalUser(any())).thenReturn(user);
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
        when(userHelper.getCurrentInternalUser(any())).thenReturn(null);

        boolean result = accessVerifier.verifyAccessesSelf(targetUserId, principal);

        assertFalse(result);
    }
}
