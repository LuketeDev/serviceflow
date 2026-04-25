package io.github.lukete.serviceflow.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import io.github.lukete.serviceflow.appointment.domain.entity.Appointment;
import io.github.lukete.serviceflow.appointment.domain.enums.AppointmentStatus;
import io.github.lukete.serviceflow.service.domain.entity.ServiceEntity;
import io.github.lukete.serviceflow.user.domain.entity.User;

class AppointmentControllerIT extends IntegrationTestBase {
    @Test
    void shouldCreateAppointmentSuccessfully() throws Exception {
        User user = userRepository.save(TestDataFactory.user("appointment-owner@example.com"));
        ServiceEntity service = serviceRepository.save(TestDataFactory.service(user, "Consultation", 45));
        LocalDate appointmentDate = LocalDate.of(2026, 5, 12);

        String request = createAppointmentRequest(user, service, appointmentDate, "09:00");

        mockMvc.perform(post("/api/appointments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.userId").value(user.getId().toString()))
                .andExpect(jsonPath("$.serviceId").value(service.getId().toString()))
                .andExpect(jsonPath("$.customerName").value("Grace Hopper"))
                .andExpect(jsonPath("$.customerPhone").value("+15555550200"))
                .andExpect(jsonPath("$.appointmentDate").value(appointmentDate.toString()))
                .andExpect(jsonPath("$.startTime").value("09:00"))
                .andExpect(jsonPath("$.endTime").value("09:45"))
                .andExpect(jsonPath("$.status").value("SCHEDULED"))
                .andExpect(jsonPath("$.notes").value("Bring documents"));
    }

    @Test
    void shouldFailWhenServiceDoesNotBelongToUser() throws Exception {
        User user = userRepository.save(TestDataFactory.user("appointment-user@example.com"));
        User otherUser = userRepository.save(TestDataFactory.user("appointment-other@example.com"));
        ServiceEntity otherService = serviceRepository.save(TestDataFactory.service(otherUser, "Other service", 30));

        String request = createAppointmentRequest(user, otherService, LocalDate.of(2026, 5, 13), "10:00");

        mockMvc.perform(post("/api/appointments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Service with ID " + otherService.getId()
                        + " does not belong to user with ID " + user.getId()))
                .andExpect(jsonPath("$.path").value("/api/appointments"));
    }

    @Test
    void shouldReturnConflictWhenTimeOverlaps() throws Exception {
        User user = userRepository.save(TestDataFactory.user("conflict-owner@example.com"));
        ServiceEntity service = serviceRepository.save(TestDataFactory.service(user, "Consultation", 60));
        LocalDate appointmentDate = LocalDate.of(2026, 5, 14);
        appointmentRepository.save(TestDataFactory.appointment(
                user,
                service,
                appointmentDate,
                LocalTime.of(9, 0),
                AppointmentStatus.SCHEDULED));

        String request = createAppointmentRequest(user, service, appointmentDate, "09:30");

        mockMvc.perform(post("/api/appointments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("The selected time slot (09:30 - 10:30) overlaps with an existing appointment"))
                .andExpect(jsonPath("$.path").value("/api/appointments"));
    }

    @Test
    void shouldCancelAppointmentSuccessfully() throws Exception {
        Appointment appointment = savedAppointment("cancel-owner@example.com", AppointmentStatus.SCHEDULED);

        mockMvc.perform(patch("/api/appointments/{id}/cancel", appointment.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(appointment.getId().toString()))
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    @Test
    void shouldCompleteAppointmentSuccessfully() throws Exception {
        Appointment appointment = savedAppointment("complete-owner@example.com", AppointmentStatus.SCHEDULED);

        mockMvc.perform(patch("/api/appointments/{id}/complete", appointment.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(appointment.getId().toString()))
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    void shouldFailInvalidStatusTransition() throws Exception {
        Appointment appointment = savedAppointment("invalid-transition-owner@example.com", AppointmentStatus.CANCELLED);

        mockMvc.perform(patch("/api/appointments/{id}/complete", appointment.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Cannot change appointment status from CANCELLED to COMPLETED"))
                .andExpect(jsonPath("$.path").value("/api/appointments/" + appointment.getId() + "/complete"));
    }

    private Appointment savedAppointment(String userEmail, AppointmentStatus status) {
        User user = userRepository.save(TestDataFactory.user(userEmail));
        ServiceEntity service = serviceRepository.save(TestDataFactory.service(user, "Consultation", 45));
        return appointmentRepository.save(TestDataFactory.appointment(
                user,
                service,
                LocalDate.of(2026, 5, 15),
                LocalTime.of(11, 0),
                status));
    }

    private static String createAppointmentRequest(
            User user,
            ServiceEntity service,
            LocalDate appointmentDate,
            String startTime) {
        return """
                {
                  "userId": "%s",
                  "serviceId": "%s",
                  "customerName": "Grace Hopper",
                  "customerPhone": "+15555550200",
                  "appointmentDate": "%s",
                  "startTime": "%s",
                  "notes": "Bring documents"
                }
                """.formatted(user.getId(), service.getId(), appointmentDate, startTime);
    }
}
