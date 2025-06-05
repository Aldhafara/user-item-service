package com.aldhafara.useritemservice.integration;

import com.aldhafara.useritemservice.dto.AuthRequest;
import com.aldhafara.useritemservice.dto.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public class TestAuthHelper {

    @Autowired
    private TestRestTemplate restTemplate;

    public ResponseEntity<Void> register(String login, String password) {
        var request = new AuthRequest(login, password);
        return restTemplate.postForEntity("/register", request, Void.class);
    }


    public String registerAndLogin(String login, String password) {
        var request = new AuthRequest(login, password);
        var registerResponse = restTemplate.postForEntity("/register", request, String.class);
        if (!registerResponse.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Register failed: " + registerResponse.getBody());
        }

        var loginResponse = restTemplate.postForEntity("/login", request, AuthResponse.class);
        if (!loginResponse.getStatusCode().is2xxSuccessful() || loginResponse.getBody() == null) {
            throw new RuntimeException("Login failed: " + loginResponse.getBody());
        }

        return loginResponse.getBody().token();
    }

    public HttpHeaders headersWithToken(String token) {
        var headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public HttpEntity<?> entityWithAuth(Object body, String token) {
        return new HttpEntity<>(body, headersWithToken(token));
    }

    public HttpEntity<?> emptyEntityWithAuth(String token) {
        return new HttpEntity<>(headersWithToken(token));
    }
}

