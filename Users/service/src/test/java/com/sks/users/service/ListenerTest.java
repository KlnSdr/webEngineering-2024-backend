package com.sks.users.service;

import com.sks.users.api.UserDTO;
import com.sks.users.api.UsersRequestMessage;
import com.sks.users.api.UsersRequestType;
import com.sks.users.api.UsersSender;
import com.sks.users.service.data.TokenService;
import com.sks.users.service.data.UsersEntity;
import com.sks.users.service.data.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class ListenerTest {
    @Mock
    private UsersService usersService;

    @Mock
    private UsersSender usersSender;

    @Mock
    private TokenService tokenService;

    private Listener listener;

    @BeforeEach
    void setUp() {
        usersSender = mock(UsersSender.class);
        usersService = mock(UsersService.class);
        tokenService = mock(TokenService.class);
        listener = new Listener(usersSender, usersService, tokenService);
    }

    @Test
    void listenSendsUserWhenGetRequestAndUserExists() {
        final UsersRequestMessage message = new UsersRequestMessage();
        message.setRequestType(UsersRequestType.GET_BY_ID);
        message.setUserId(1L);

        UsersEntity user = new UsersEntity();
        user.setId(1L);
        when(usersService.findById(1L)).thenReturn(Optional.of(user));

        listener.listen(message);

        verify(usersSender).sendResponse(eq(message), argThat(response -> {
            UserDTO userDTO = response.getUser();
            return userDTO != null && userDTO.getUserId().equals(user.getId());
        }));
    }

    @Test
    void listenSendsEmptyResponseWhenGetRequestAndUserDoesNotExist() {
        UsersRequestMessage message = mock(UsersRequestMessage.class);
        when(message.getRequestType()).thenReturn(UsersRequestType.GET_BY_ID);
        when(message.getUserId()).thenReturn(1L);
        when(usersService.findById(1L)).thenReturn(Optional.empty());

        listener.listen(message);

        verify(usersSender).sendResponse(eq(message), argThat(response -> response.getUser() == null));
    }

    @Test
    void listenSendsUserWhenGetRequestAndIdpUserExists() {
        final UsersRequestMessage message = new UsersRequestMessage();
        message.setRequestType(UsersRequestType.GET_BY_IDP);
        message.setUserId(1L);

        UsersEntity user = new UsersEntity();
        user.setId(1L);
        when(usersService.findByIdpHash(any())).thenReturn(Optional.of(user));

        listener.listen(message);

        verify(usersSender).sendResponse(eq(message), argThat(response -> {
            UserDTO userDTO = response.getUser();
            return userDTO != null && userDTO.getUserId().equals(user.getId());
        }));
    }

    @Test
    void listenSendsEmptyResponseWhenGetRequestAndIdpUserDoesNotExist() {
        UsersRequestMessage message = mock(UsersRequestMessage.class);
        when(message.getRequestType()).thenReturn(UsersRequestType.GET_BY_IDP);
        when(message.getUserId()).thenReturn(1L);
        when(usersService.findByIdpHash(any())).thenReturn(Optional.empty());

        listener.listen(message);

        verify(usersSender).sendResponse(eq(message), argThat(response -> response.getUser() == null));
    }

    @Test
    void listenCreatesAndSendsUserWhenCreateRequestAndUserDoesNotExist() {
        UsersRequestMessage message = mock(UsersRequestMessage.class);
        when(message.getRequestType()).thenReturn(UsersRequestType.CREATE);
        when(message.getIdpUserId()).thenReturn(1L);
        when(message.getUserName()).thenReturn("UserName");
        when(usersService.findByIdpHash(anyString())).thenReturn(Optional.empty());
        UsersEntity savedUser = new UsersEntity();
        savedUser.setDisplayName("UserName");
        when(usersService.save(any(UsersEntity.class))).thenReturn(savedUser);

        listener.listen(message);

        verify(usersSender).sendResponse(eq(message), argThat(response -> {
            UserDTO userDTO = response.getUser();
            return userDTO != null && userDTO.getUserName().equals("UserName");
        }));
    }

    @Test
    void listenSendsExistingUserWhenCreateRequestAndUserExists() {
        UsersRequestMessage message = mock(UsersRequestMessage.class);
        when(message.getRequestType()).thenReturn(UsersRequestType.CREATE);
        when(message.getIdpUserId()).thenReturn(1L);
        UsersEntity existingUser = new UsersEntity();
        existingUser.setId(1L);
        when(usersService.findByIdpHash(anyString())).thenReturn(Optional.of(existingUser));
        when(usersService.hashId(anyLong())).thenReturn("hash");

        listener.listen(message);

        verify(usersSender).sendResponse(eq(message), argThat(response -> {
            UserDTO userDTO = response.getUser();
            return userDTO != null && userDTO.getUserId().equals(existingUser.getId());
        }));
    }

}
