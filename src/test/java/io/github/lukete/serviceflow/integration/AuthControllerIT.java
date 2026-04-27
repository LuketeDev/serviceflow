package io.github.lukete.serviceflow.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.github.lukete.serviceflow.user.domain.entity.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class AuthControllerIT extends IntegrationTestBase {
    private final PasswordEncoder passwordEncoder;

    @Test
    void shouldLoginSuccessfully() throws Exception {
        String rawPassword = "12345678";

        User user = new User();
        user.setName("Lukete");
        user.setEmail("lukete@example.com");
        user.setPhone("62999999999");
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setActive(true);

        userRepository.save(user);

        String body = """
                {
                    "email":"lukete@example.com",
                    "password":"12345678"
                }
                """;
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }
}
