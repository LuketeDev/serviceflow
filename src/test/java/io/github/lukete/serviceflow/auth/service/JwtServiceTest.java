package io.github.lukete.serviceflow.auth.service;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.lukete.serviceflow.auth.config.JwtProperties;
import io.github.lukete.serviceflow.user.domain.entity.User;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {
    private JwtService jwtService;

    @BeforeEach
    void setup() {
        JwtProperties properties = new JwtProperties(
                "my-super-secret-key-my-super-secret-key",
                60);

        jwtService = new JwtService(properties);
    }

    @Test
    void shouldGenerateTokenAndExtractEmail() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("lukete@example.com");

        String token = jwtService.generateToken(user);

        assertThat(jwtService.extractEmail(token)).isEqualTo("lukete@example.com");
        assertThat(jwtService.extractUserId(token)).isEqualTo(user.getId());
        assertThat(jwtService.isTokenValid(token, user)).isTrue();
    }
}
