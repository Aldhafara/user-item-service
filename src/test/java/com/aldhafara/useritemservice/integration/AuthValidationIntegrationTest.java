package com.aldhafara.useritemservice.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AuthValidationIntegrationTest {

    @Autowired
    private TestAuthHelper authHelper;

    @Test
    void shouldRejectEmptyLogin() {
        var response = authHelper.register("", "ValidPass123");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldRejectBlankLogin() {
        var response = authHelper.register(" ", "ValidPass123");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldRejectTooShortPassword() {
        var response = authHelper.register("userV1@test.com", "A1b");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldRejectPasswordWithoutUppercase() {
        var response = authHelper.register("userV2@test.com", "lowercase123");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldRejectPasswordWithoutLowercase() {
        var response = authHelper.register("userV3@test.com", "UPPERCASE123");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldRejectPasswordWithoutDigit() {
        var response = authHelper.register("userV4@test.com", "NoDigitsHere");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldAcceptValidCredentials() {
        var response = authHelper.register("valid.user@test.com", "StrongPass1");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}

