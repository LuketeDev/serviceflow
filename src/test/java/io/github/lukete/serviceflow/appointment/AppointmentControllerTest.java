package io.github.lukete.serviceflow.appointment;

import org.junit.jupiter.api.Test;

import io.github.lukete.serviceflow.appointment.domain.entity.Appointment;
import io.github.lukete.serviceflow.appointment.domain.enums.AppointmentStatus;
import io.github.lukete.serviceflow.service.domain.entity.ServiceEntity;
import io.github.lukete.serviceflow.support.IntegrationTestBase;
import io.github.lukete.serviceflow.user.domain.entity.User;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.http.MediaType;

class AppointmentControllerTest extends IntegrationTestBase {

    @Test
    void shouldCreateAppointmentSuccessfully() throws Exception {
        User user = userRepository.save(buildUser());
        ServiceEntity service = serviceRepository.save(buildService(user));

        String requestBody = """
                {
                  "userId": "%s",
                  "serviceId": "%s",
                  "customerName": "João",
                  "customerPhone": "62988888888",
                  "appointmentDate": "2026-04-22",
                  "startTime": "14:00",
                  "notes": "Primeiro atendimento"
                }
                """.formatted(user.getId(), service.getId());

        mockMvc.perform(post("/api/appointments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerName").value("João"))
                .andExpect(jsonPath("$.status").value("SCHEDULED"))
                .andExpect(jsonPath("$.endTime").value("14:45"));
    }

    @Test
    void shouldReturnConflictWhenAppointmentTimeOverlaps() throws Exception {
        User user = userRepository.save(buildUser());
        ServiceEntity service = serviceRepository.save(buildService(user));

        Appointment existing = new Appointment();
        existing.setUser(user);
        existing.setService(service);
        existing.setCustomerName("Maria");
        existing.setAppointmentDate(LocalDate.of(2026, 4, 22));
        existing.setStartTime(LocalTime.of(14, 0));
        existing.setEndTime(LocalTime.of(14, 45));
        existing.setStatus(AppointmentStatus.SCHEDULED);
        appointmentRepository.save(existing);

        String requestBody = """
                {
                  "userId": "%s",
                  "serviceId": "%s",
                  "customerName": "João",
                  "customerPhone": "62988888888",
                  "appointmentDate": "2026-04-22",
                  "startTime": "14:30",
                  "notes": "Conflito"
                }
                """.formatted(user.getId(), service.getId());

        mockMvc.perform(post("/api/appointments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    protected User buildUser() {
        User user = new User();
        user.setName("Lucas");
        user.setEmail("lucas@example.com");
        user.setPhone("62999999999");
        user.setPassword("$2a$10$hashfake");
        user.setActive(true);
        return user;
    }

    protected ServiceEntity buildService(User user) {
        ServiceEntity service = new ServiceEntity();
        service.setName("Corte");
        service.setDescription("Corte simples");
        service.setDurationInMinutes(45);
        service.setPrice(new BigDecimal("35.00"));
        service.setActive(true);
        service.setUser(user);
        return service;
    }
}
