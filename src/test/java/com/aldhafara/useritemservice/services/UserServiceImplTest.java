package com.aldhafara.useritemservice.services;

import com.aldhafara.useritemservice.entities.User;
import com.aldhafara.useritemservice.exceptions.UserNotFoundException;
import com.aldhafara.useritemservice.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldCreateUser() {
        // given
        User user = new User("login123");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // when
        User created = userService.createUser("login123", "password");

        // then
        assertEquals("login123", created.getLogin());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldEncodePasswordWhenCreatingUser() {
        // given
        String rawPassword = "plainPassword";
        User user = new User("login123");

        when(passwordEncoder.encode(rawPassword)).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        User created = userService.createUser("login123", rawPassword);

        // then
        assertNotNull(created.getPassword());
        assertNotEquals(rawPassword, created.getPassword());
        assertEquals("encodedPassword", created.getPassword());
    }

    @Test
    void shouldReturnUserById() {
        // given
        UUID id = UUID.randomUUID();
        User user = new User("test_login");
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        // when
        Optional<User> result = userService.findById(id);

        // then
        assertTrue(result.isPresent());
        assertEquals("test_login", result.get().getLogin());
    }

    @Test
    void shouldReturnEmptyWhenUserNotFoundById() {
        // given
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // when
        Optional<User> result = userService.findById(id);

        // then
        assertFalse(result.isPresent());
    }

    @Test
    void shouldDeleteUser() {
        // given
        UUID id = UUID.randomUUID();

        // when
        userService.deleteUser(id);

        // then
        verify(userRepository).deleteById(id);
    }

    @Test
    void shouldAddItemToExistingUser() {
        // given
        UUID userId = UUID.randomUUID();
        User user = new User("test_login");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // when
        userService.addItemToUser(userId, "item_1");

        // then
        assertEquals(1, user.getItems().size());
        assertEquals("item_1", user.getItems().get(0).getName());
        verify(userRepository).save(user);
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        // given
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when / then
        assertThrows(UserNotFoundException.class, () -> userService.addItemToUser(userId, "item_name"));
        verify(userRepository, never()).save(any());
    }
}
