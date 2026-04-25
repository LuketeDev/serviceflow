package io.github.lukete.serviceflow.integration;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import io.github.lukete.serviceflow.service.domain.entity.ServiceEntity;
import io.github.lukete.serviceflow.user.domain.entity.User;

class ServiceControllerIT extends IntegrationTestBase {
    @Test
    void shouldCreateServiceSuccessfully() throws Exception {
        User user = userRepository.save(TestDataFactory.user("service-owner@example.com"));

        String request = """
                {
                  "name": "Consultation",
                  "description": "Initial appointment",
                  "durationInMinutes": 45,
                  "price": 99.90,
                  "userId": "%s"
                }
                """.formatted(user.getId());

        mockMvc.perform(post("/api/services")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Consultation"))
                .andExpect(jsonPath("$.description").value("Initial appointment"))
                .andExpect(jsonPath("$.durationInMinutes").value(45))
                .andExpect(jsonPath("$.price").value(99.90))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.userId").value(user.getId().toString()));
    }

    @Test
    void shouldFailWhenUserDoesNotExist() throws Exception {
        UUID missingUserId = UUID.randomUUID();
        String request = """
                {
                  "name": "Consultation",
                  "description": "Initial appointment",
                  "durationInMinutes": 45,
                  "price": 99.90,
                  "userId": "%s"
                }
                """.formatted(missingUserId);

        mockMvc.perform(post("/api/services")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("User not found with id: " + missingUserId))
                .andExpect(jsonPath("$.path").value("/api/services"));
    }

    @Test
    void shouldListServicesByUser() throws Exception {
        User user = userRepository.save(TestDataFactory.user("list-owner@example.com"));
        User otherUser = userRepository.save(TestDataFactory.user("other-owner@example.com"));

        ServiceEntity haircut = serviceRepository.save(TestDataFactory.service(user, "Haircut", 30));
        ServiceEntity coloring = serviceRepository.save(TestDataFactory.service(user, "Coloring", 90));
        serviceRepository.save(TestDataFactory.service(otherUser, "Massage", 60));

        mockMvc.perform(get("/api/services")
                .param("userId", user.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].userId").value(user.getId().toString()))
                .andExpect(jsonPath("$[1].userId").value(user.getId().toString()))
                .andExpect(jsonPath("$[?(@.id == '%s')]".formatted(haircut.getId()), hasSize(1)))
                .andExpect(jsonPath("$[?(@.id == '%s')]".formatted(coloring.getId()), hasSize(1)));
    }
}
