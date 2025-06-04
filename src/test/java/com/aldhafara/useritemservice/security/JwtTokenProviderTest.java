package com.aldhafara.useritemservice.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();
    }

    @Test
    void shouldGenerateAndValidateToken() {
        // given
        UUID userId = UUID.randomUUID();

        // when
        String token = jwtTokenProvider.generateToken(userId);

        // then
        assertNotNull(token);
        assertTrue(jwtTokenProvider.validateToken(token));
        assertEquals(userId, jwtTokenProvider.getUserIdFromToken(token));
    }

    @Test
    void shouldRejectInvalidToken() {
        // given
        String invalidToken = "this.is.not.valid";

        // then
        assertFalse(jwtTokenProvider.validateToken(invalidToken));
    }
}
