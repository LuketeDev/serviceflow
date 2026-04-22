package io.github.lukete.serviceflow.user;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import io.github.lukete.serviceflow.support.IntegrationTestBase;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest extends IntegrationTestBase {
        @Test
        void shouldCreateUserSuccessfully() throws Exception {
                String requestBody = """
                                {
                                  "name": "Lucas",
                                  "email": "lucas@example.com",
                                  "phone": "62999999999",
                                  "password": "123456"
                                }
                                """;

                mockMvc.perform(post("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").exists())
                                .andExpect(jsonPath("$.name").value("Lucas"))
                                .andExpect(jsonPath("$.email").value("lucas@example.com"))
                                .andExpect(jsonPath("$.password").doesNotExist());
        }

        @Test
        void shouldReturnConflictWhenEmailAlreadyExists() throws Exception {
                String requestBody = """
                                {
                                  "name": "Lucas",
                                  "email": "lucas@example.com",
                                  "phone": "62999999999",
                                  "password": "123456"
                                }
                                """;
                mockMvc.perform(post("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                                .andExpect(status().isCreated());

                mockMvc.perform(post("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                                .andExpect(status().isConflict())
                                .andExpect(jsonPath("$.status").value(409));
        }

        @Test
        void shouldReturnBadRequestWhenUserPayloadIsInvalid() throws Exception {
                String requestBody = """
                                {
                                  "name": "",
                                  "email": "email-invalido",
                                  "password": ""
                                }
                                """;

                mockMvc.perform(post("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message").value("Validation failed"))
                                .andExpect(jsonPath("$.errors.name").exists())
                                .andExpect(jsonPath("$.errors.email").exists())
                                .andExpect(jsonPath("$.errors.password").exists());
        }
}