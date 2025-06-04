package com.aldhafara.useritemservice.controllers;

import com.aldhafara.useritemservice.dto.AuthRequest;
import com.aldhafara.useritemservice.dto.AuthResponse;
import com.aldhafara.useritemservice.exceptions.InvalidCredentialsException;
import com.aldhafara.useritemservice.exceptions.LoginAlreadyInUseException;
import com.aldhafara.useritemservice.services.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthControllerTest {

    private final AuthServiceImpl authService = mock(AuthServiceImpl.class);
    private final AuthController controller = new AuthController(authService);

    @Test
    void shouldRegisterAndReturn204() {
        // given
        AuthRequest req = new AuthRequest("login", "pass");

        // when
        ResponseEntity<?> response = controller.register(req);

        // then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void shouldLoginAndReturnToken() {
        // given
        String token = "fake-jwt-token";
        AuthRequest req = new AuthRequest("login", "pass");
        when(authService.authenticate(req.login(), req.password())).thenReturn(token);

        // when
        ResponseEntity<?> response = controller.login(req);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        AuthResponse authResponse = (AuthResponse) response.getBody();
        assertEquals(token, authResponse.token());
    }

    @Test
    void shouldReturnUnauthorizedWhenLoginFails() {
        // given
        AuthRequest req = new AuthRequest("login", "badpass");
        when(authService.authenticate(req.login(), req.password()))
                .thenThrow(new RuntimeException("Invalid credentials"));

        // when / then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> controller.login(req));
        assertEquals("Invalid credentials", exception.getMessage());
    }

    @Test
    void shouldReturnConflictWhenLoginAlreadyUsed() {
        // given
        AuthRequest req = new AuthRequest("login", "pass");
        doThrow(new LoginAlreadyInUseException())
                .when(authService).register(req.login(), req.password());

        // when
        ResponseEntity<?> response = controller.register(req);

        // then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Login already in use", response.getBody());
    }

    @Test
    void shouldReturnUnauthorizedWhenInvalidCredentialsUsed() {
        // given
        AuthRequest req = new AuthRequest("login", "pass");
        doThrow(new InvalidCredentialsException())
                .when(authService).authenticate(req.login(), req.password());

        // when
        ResponseEntity<?> response = controller.login(req);

        // then
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody());
    }
}
