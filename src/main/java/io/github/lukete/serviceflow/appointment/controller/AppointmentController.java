package io.github.lukete.serviceflow.appointment.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.lukete.serviceflow.appointment.dto.AppointmentResponse;
import io.github.lukete.serviceflow.appointment.dto.CreateAppointmentRequest;
import io.github.lukete.serviceflow.appointment.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

    @PostMapping()
    public AppointmentResponse createAppointment(@Valid @RequestBody CreateAppointmentRequest request) {
        return appointmentService.createAppointment(request);
    }

    @GetMapping
    public List<AppointmentResponse> getAll(
            @RequestParam(required = false) UUID userId,
            @RequestParam(required = false) LocalDate appointmentDate) {

        if (userId != null && appointmentDate != null) {
            return appointmentService.findByUserIdAndDate(userId, appointmentDate);
        }
        if (userId != null) {
            return appointmentService.findByUserId(userId);
        }
        if (appointmentDate != null) {
            return appointmentService.findByDate(appointmentDate);
        }

        return appointmentService.findAll();
    }

    @GetMapping("/{id}")
    public AppointmentResponse getById(@PathVariable UUID id) {
        return appointmentService.findById(id);
    }

    @PatchMapping("/{id}/cancel")
    public AppointmentResponse cancel(@PathVariable UUID id) {
        return appointmentService.cancel(id);
    }

    @PatchMapping("/{id}/complete")
    public AppointmentResponse complete(@PathVariable UUID id) {
        return appointmentService.complete(id);
    }
}