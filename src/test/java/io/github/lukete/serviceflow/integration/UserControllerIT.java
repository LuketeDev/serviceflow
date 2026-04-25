package io.github.lukete.serviceflow.integration;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.hasKey;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class UserControllerIT extends IntegrationTestBase {
    @Test
    void shouldCreateUserSuccessfully() throws Exception {
        String request = """
                {
                  "name": "Ada Lovelace",
                  "email": "ada@example.com",
                  "phone": "+15555550100",
                  "password": "secret123"
                }
                """;

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Ada Lovelace"))
                .andExpect(jsonPath("$.email").value("ada@example.com"))
                .andExpect(jsonPath("$.phone").value("+15555550100"))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$", not(hasKey("password"))));
    }

    @Test
    void shouldReturnConflictWhenEmailAlreadyExists() throws Exception {
        userRepository.save(TestDataFactory.user("duplicate@example.com"));

        String request = """
                {
                  "name": "Duplicate User",
                  "email": "duplicate@example.com",
                  "phone": "+15555550101",
                  "password": "secret123"
                }
                """;

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("User already exists with the email: duplicate@example.com"))
                .andExpect(jsonPath("$.path").value("/api/users"));
    }

    @Test
    void shouldReturnBadRequestWhenPayloadIsInvalid() throws Exception {
        String request = """
                {
                  "name": "",
                  "email": "not-an-email",
                  "phone": "+15555550102",
                  "password": ""
                }
                """;

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.name").exists())
                .andExpect(jsonPath("$.errors.email").exists())
                .andExpect(jsonPath("$.errors.password").exists());
    }
}
