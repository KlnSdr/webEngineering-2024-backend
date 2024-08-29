package com.sks.gateway.users;

import com.sks.users.api.UserDTO;
import com.sks.users.api.UsersRequestMessage;
import com.sks.users.api.UsersResponseMessage;
import com.sks.users.api.UsersSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UsersResourceTest {

    @Mock
    private UsersSender sender;

    @InjectMocks
    private UsersResource usersResource;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCurrentUserReturnsPrincipalWhenPrincipalIsNotNull() {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("user");

        Principal result = usersResource.getCurrentUser(principal);

        assertEquals(principal, result);
    }

    @Test
    void getCurrentUserReturnsNullWhenPrincipalIsNull() {
        Principal result = usersResource.getCurrentUser(null);

        assertNull(result);
    }

    @Test
    void getUserByIdReturnsUserWhenUserExists() {
        Long userId = 1L;
        UserDTO user = new UserDTO();
        UsersResponseMessage responseMessage = new UsersResponseMessage();
        responseMessage.setUser(user);
        when(sender.sendRequest(any(UsersRequestMessage.class))).thenReturn(responseMessage);

        UserDTO result = usersResource.getUserById(userId);

        assertEquals(user, result);
    }

    @Test
    void getUserByIdThrowsExceptionWhenUserDoesNotExist() {
        Long userId = 1L;
        UsersResponseMessage responseMessage = new UsersResponseMessage();
        when(sender.sendRequest(any(UsersRequestMessage.class))).thenReturn(responseMessage);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            usersResource.getUserById(userId);
        });

        assertEquals("User with id " + userId + " not found", exception.getReason());
    }

    @Test
    void getUserByIdThrowsExceptionOnMessageError() {
        Long userId = 1L;
        UsersResponseMessage responseMessage = new UsersResponseMessage();
        responseMessage.setDidError(true);
        doThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error")).when(sender).sendRequest(any(UsersRequestMessage.class));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            usersResource.getUserById(userId);
        });

        assertEquals("Internal Server Error", exception.getReason());
    }
}
