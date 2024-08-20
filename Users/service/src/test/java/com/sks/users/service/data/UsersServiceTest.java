package com.sks.users.service.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class UsersServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @InjectMocks
    private UsersService usersService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllReturnsListOfUsers() {
        UsersEntity user = new UsersEntity();
        when(usersRepository.findAll()).thenReturn(List.of(user));

        List<UsersEntity> result = usersService.getAll();

        assertEquals(1, result.size());
        assertEquals(user, result.getFirst());
    }

    @Test
    void getAllReturnsEmptyListWhenNoUsers() {
        when(usersRepository.findAll()).thenReturn(Collections.emptyList());

        List<UsersEntity> result = usersService.getAll();

        assertEquals(0, result.size());
    }

    @Test
    void saveReturnsSavedUser() {
        UsersEntity user = new UsersEntity();
        when(usersRepository.save(user)).thenReturn(user);

        UsersEntity result = usersService.save(user);

        assertEquals(user, result);
    }

    @Test
    void findByIdReturnsUserWhenUserExists() {
        UsersEntity user = new UsersEntity();
        when(usersRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<UsersEntity> result = usersService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void findByIdReturnsEmptyWhenUserDoesNotExist() {
        when(usersRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<UsersEntity> result = usersService.findById(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void findByIdpHashReturnsUserWhenHashMatches() {
        UsersEntity user = new UsersEntity();
        String hash = "someHash";
        when(usersRepository.findByIdpHashEquals(hash)).thenReturn(Optional.of(user));

        Optional<UsersEntity> result = usersService.findByIdpHash(hash);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void findByIdpHashReturnsEmptyWhenHashDoesNotMatch() {
        String hash = "someHash";
        when(usersRepository.findByIdpHashEquals(hash)).thenReturn(Optional.empty());

        Optional<UsersEntity> result = usersService.findByIdpHash(hash);

        assertTrue(result.isEmpty());
    }
}
