package io.github.lukete.serviceflow.appointment.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import io.github.lukete.serviceflow.appointment.domain.enums.AppointmentStatus;

public record AppointmentResponse(
        UUID id,
        UUID userId,
        UUID serviceId,
        String customerName,
        String customerPhone,
        LocalDate appointmentDate,
        LocalTime startTime,
        LocalTime endTime,
        AppointmentStatus status,
        String notes) {
}