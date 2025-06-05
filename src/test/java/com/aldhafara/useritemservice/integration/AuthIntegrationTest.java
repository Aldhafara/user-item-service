package com.aldhafara.useritemservice.integration;

import com.aldhafara.useritemservice.dto.AuthRequest;
import com.aldhafara.useritemservice.dto.AuthResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AuthIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldRegisterAndLoginSuccessfully() {
        var request = new AuthRequest("user@test.com", "Pass123");

        var registerResponse = restTemplate.postForEntity("/register", request, Void.class);
        assertEquals(HttpStatus.NO_CONTENT, registerResponse.getStatusCode());

        var loginResponse = restTemplate.postForEntity("/login", request, AuthResponse.class);
        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertNotNull(loginResponse.getBody());
        assertNotNull(loginResponse.getBody().token());
        assertFalse(loginResponse.getBody().token().isBlank());
    }

    @Test
    void shouldReturn401ForInvalidPassword() {
        var request = new AuthRequest("user2@test.com", "ValidPass123");
        restTemplate.postForEntity("/register", request, Void.class);

        var badLogin = new AuthRequest("user2@test.com", "WrongPass");
        var response = restTemplate.postForEntity("/login", badLogin, String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void shouldReturn401ForNonexistentUser() {
        var response = restTemplate.postForEntity("/login", new AuthRequest("nosuchuser@test.com", "Pass123"), String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void shouldReturn409WhenRegisteringExistingUser() {
        var request = new AuthRequest("user3@test.com", "Pass123");
        restTemplate.postForEntity("/register", request, Void.class);

        var secondAttempt = restTemplate.postForEntity("/register", request, String.class);

        assertEquals(HttpStatus.CONFLICT, secondAttempt.getStatusCode());
    }
}
