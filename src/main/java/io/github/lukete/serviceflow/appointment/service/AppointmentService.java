package io.github.lukete.serviceflow.appointment.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import io.github.lukete.serviceflow.appointment.domain.entity.Appointment;
import io.github.lukete.serviceflow.appointment.domain.enums.AppointmentStatus;
import io.github.lukete.serviceflow.appointment.dto.AppointmentResponse;
import io.github.lukete.serviceflow.appointment.dto.CreateAppointmentRequest;
import io.github.lukete.serviceflow.exceptions.InvalidStatusTransitionException;
import io.github.lukete.serviceflow.appointment.repository.AppointmentRepository;
import io.github.lukete.serviceflow.exceptions.ResourceDoesNotBelongException;
import io.github.lukete.serviceflow.exceptions.ResourceNotFoundException;
import io.github.lukete.serviceflow.exceptions.ScheduleConflictException;
import io.github.lukete.serviceflow.service.domain.entity.ServiceEntity;
import io.github.lukete.serviceflow.service.repository.ServiceRepository;
import io.github.lukete.serviceflow.user.domain.entity.User;
import io.github.lukete.serviceflow.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private static final String APPOINTMENT_NOT_FOUND = "Appointment not found with id: ";
    private static final String SERVICE_NOT_FOUND = "Service not found with id: ";
    private static final String USER_NOT_FOUND = "User not found with id: ";

    private final AppointmentRepository appointmentRepository;
    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;

    public AppointmentResponse createAppointment(CreateAppointmentRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND + request.userId()));

        ServiceEntity service = serviceRepository.findById(request.serviceId())
                .orElseThrow(() -> new ResourceNotFoundException(SERVICE_NOT_FOUND + request.serviceId()));

        if (!service.getUser().getId().equals(request.userId())) {
            throw new ResourceDoesNotBelongException(
                    "Service with ID " + request.serviceId() + " does not belong to user with ID " + request.userId());
        }

        LocalTime endTime = request.startTime().plusMinutes(service.getDurationInMinutes());

        if (appointmentRepository.existsConflict(request.userId(), request.appointmentDate(), request.startTime(),
                endTime)) {
            throw new ScheduleConflictException("The selected time slot (" + request.startTime() + " - " + endTime
                    + ") overlaps with an existing appointment");
        }

        Appointment appointment = new Appointment();
        appointment.setUser(user);
        appointment.setService(service);
        appointment.setCustomerName(request.customerName());
        appointment.setCustomerPhone(request.customerPhone());
        appointment.setAppointmentDate(request.appointmentDate());
        appointment.setStartTime(request.startTime());
        appointment.setEndTime(endTime);
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointment.setNotes(request.notes());

        Appointment saved = appointmentRepository.save(appointment);
        return toResponse(saved);
    }

    public List<AppointmentResponse> findAll() {
        return appointmentRepository.findAll().stream().map(this::toResponse).toList();
    }

    public AppointmentResponse findById(UUID id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(APPOINTMENT_NOT_FOUND + id));
        return toResponse(appointment);
    }

    public List<AppointmentResponse> findByUserId(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND + userId));

        return appointmentRepository.findByUser(user).stream().map(this::toResponse).toList();
    }

    public List<AppointmentResponse> findByDate(LocalDate date) {
        return appointmentRepository.findByAppointmentDate(date).stream().map(this::toResponse).toList();
    }

    public List<AppointmentResponse> findByUserIdAndDate(UUID userId, LocalDate appointmentDate) {
        return appointmentRepository.findByUserIdAndAppointmentDate(userId, appointmentDate)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public AppointmentResponse cancel(UUID id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(APPOINTMENT_NOT_FOUND + id));

        ensureScheduled(appointment, AppointmentStatus.CANCELLED);

        appointment.setStatus(AppointmentStatus.CANCELLED);

        Appointment saved = appointmentRepository.save(appointment);
        return toResponse(saved);
    }

    public AppointmentResponse complete(UUID id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(APPOINTMENT_NOT_FOUND + id));

        ensureScheduled(appointment, AppointmentStatus.COMPLETED);

        appointment.setStatus(AppointmentStatus.COMPLETED);

        Appointment saved = appointmentRepository.save(appointment);
        return toResponse(saved);
    }

    private void ensureScheduled(Appointment appointment, AppointmentStatus targetStatus) {
        if (appointment.getStatus() != AppointmentStatus.SCHEDULED) {
            throw new InvalidStatusTransitionException(
                    "Cannot change appointment status from " + appointment.getStatus() + " to " + targetStatus);
        }
    }

    private AppointmentResponse toResponse(Appointment saved) {
        return new AppointmentResponse(
                saved.getId(),
                saved.getUser().getId(),
                saved.getService().getId(),
                saved.getCustomerName(),
                saved.getCustomerPhone(),
                saved.getAppointmentDate(),
                saved.getStartTime(),
                saved.getEndTime(),
                saved.getStatus(),
                saved.getNotes());
    }
}
