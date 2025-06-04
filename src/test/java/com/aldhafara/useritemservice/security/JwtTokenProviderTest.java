package com.aldhafara.useritemservice.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        String username = "testuser";

        // when
        String token = jwtTokenProvider.generateToken(username);

        // then
        assertNotNull(token);
        assertTrue(jwtTokenProvider.validateToken(token));
        assertEquals(username, jwtTokenProvider.getUsernameFromToken(token));
    }

    @Test
    void shouldRejectInvalidToken() {
        // given
        String invalidToken = "this.is.not.valid";

        // then
        assertFalse(jwtTokenProvider.validateToken(invalidToken));
    }
}
