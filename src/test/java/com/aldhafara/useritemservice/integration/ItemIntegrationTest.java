package com.aldhafara.useritemservice.integration;

import com.aldhafara.useritemservice.dto.CreateItemRequest;
import com.aldhafara.useritemservice.dto.ItemResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ItemIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private TestAuthHelper authHelper;

    private String token;

    @BeforeEach
    void setUp() {
        authHelper = new TestAuthHelper(restTemplate);
        String email = "user_" + UUID.randomUUID() + "@test.com";
        token = authHelper.registerAndLogin(email, "User1234");
    }

    @Test
    void shouldCreateItemForAuthenticatedUser() {
        var request = new CreateItemRequest("Test item");

        var response = restTemplate.postForEntity(
                "/items",
                authHelper.entityWithAuth(request, token),
                CreateItemRequest.class
        );

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void shouldReturnOnlyOwnItems() {
        for (int i = 1; i <= 3; i++) {
            var request = new CreateItemRequest("Item " + i);
            restTemplate.postForEntity("/items", authHelper.entityWithAuth(request, token), CreateItemRequest.class);
        }
        var response = restTemplate.exchange(
                "/items?page=0&size=10",
                HttpMethod.GET,
                authHelper.emptyEntityWithAuth(token),
                new ParameterizedTypeReference<List<ItemResponse>>() {
                }
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        var page = response.getBody();
        assertNotNull(page);
        assertEquals(3, page.size());
    }

    @Test
    void shouldReturn401WhenCreatingItemWithoutToken() {
        var request = new CreateItemRequest("Unauthorized item");

        var response = restTemplate.postForEntity("/items", new HttpEntity<>(request), String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void shouldReturn401WhenListingItemsWithoutToken() {
        var response = restTemplate.exchange(
                "/items",
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                String.class
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
