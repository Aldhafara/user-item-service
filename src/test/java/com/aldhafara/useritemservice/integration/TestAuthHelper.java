package com.aldhafara.useritemservice.integration;

import com.aldhafara.useritemservice.dto.AuthRequest;
import com.aldhafara.useritemservice.dto.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public class TestAuthHelper {

    @Autowired
    private TestRestTemplate restTemplate;

    public String registerAndLogin(String login, String password) {
        var request = new AuthRequest(login, password);
        restTemplate.postForEntity("/register", request, Void.class);

        var loginResponse = restTemplate.postForEntity("/login", request, AuthResponse.class);
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

