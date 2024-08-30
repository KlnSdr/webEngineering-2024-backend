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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserHelperTest {

    @Mock
    private UsersSender usersSender;

    @Mock
    private Principal principal;

    @InjectMocks
    private UserHelper userHelper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCurrentInternalUserReturnsUserWhenPrincipalIsValid() {
        when(principal.getName()).thenReturn("1");
        UsersResponseMessage response = mock(UsersResponseMessage.class);
        UserDTO user = new UserDTO();
        user.setUserId(1L);
        when(usersSender.sendRequest(any(UsersRequestMessage.class))).thenReturn(response);
        when(response.getUser()).thenReturn(user);

        UserDTO result = userHelper.getCurrentInternalUser(principal);

        assertEquals(user, result);
    }

    @Test
    void getCurrentInternalUserReturnsNullWhenPrincipalIsNull() {
        UserDTO result = userHelper.getCurrentInternalUser(null);

        assertNull(result);
    }

    @Test
    void getCurrentInternalUserReturnsNullWhenUserNotFound() {
        when(principal.getName()).thenReturn("1");
        UsersResponseMessage response = mock(UsersResponseMessage.class);
        when(usersSender.sendRequest(any(UsersRequestMessage.class))).thenReturn(response);
        when(response.getUser()).thenReturn(null);

        UserDTO result = userHelper.getCurrentInternalUser(principal);

        assertNull(result);
    }

    @Test
    void getCurrentInternalUserReturnsNullWhenResponseHasError() {
        when(principal.getName()).thenReturn("1");
        UsersResponseMessage response = mock(UsersResponseMessage.class);
        when(usersSender.sendRequest(any(UsersRequestMessage.class))).thenReturn(response);
        when(response.didError()).thenReturn(true);

        UserDTO result = userHelper.getCurrentInternalUser(principal);

        assertNull(result);
    }
}
