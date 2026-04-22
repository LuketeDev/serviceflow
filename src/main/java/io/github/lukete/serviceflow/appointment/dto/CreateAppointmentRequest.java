package io.github.lukete.serviceflow.appointment.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateAppointmentRequest(
                @NotNull UUID userId,
                @NotNull UUID serviceId,
                @NotBlank String customerName,
                String customerPhone,
                @NotNull LocalDate appointmentDate,
                @NotNull LocalTime startTime,
                String notes) {
}