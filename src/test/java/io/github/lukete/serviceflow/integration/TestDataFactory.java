package io.github.lukete.serviceflow.integration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import io.github.lukete.serviceflow.appointment.domain.entity.Appointment;
import io.github.lukete.serviceflow.appointment.domain.enums.AppointmentStatus;
import io.github.lukete.serviceflow.service.domain.entity.ServiceEntity;
import io.github.lukete.serviceflow.user.domain.entity.User;

final class TestDataFactory {
    private static final String HASHED_PASSWORD = "$2a$10$7EqJtq98hPqEX7fNZaFWoOhiJwvMC.y4X2PtdO47L9Ky3UiR7w0Km";

    private TestDataFactory() {
    }

    static User user(String email) {
        User user = new User();
        user.setName("Ada Lovelace");
        user.setEmail(email);
        user.setPhone("+15555550100");
        user.setPassword(HASHED_PASSWORD);
        user.setActive(true);
        return user;
    }

    static ServiceEntity service(User user, String name, int durationInMinutes) {
        ServiceEntity service = new ServiceEntity();
        service.setName(name);
        service.setDescription(name + " description");
        service.setDurationInMinutes(durationInMinutes);
        service.setPrice(BigDecimal.valueOf(125, 2));
        service.setActive(true);
        service.setUser(user);
        return service;
    }

    static Appointment appointment(
            User user,
            ServiceEntity service,
            LocalDate date,
            LocalTime startTime,
            AppointmentStatus status) {
        Appointment appointment = new Appointment();
        appointment.setUser(user);
        appointment.setService(service);
        appointment.setCustomerName("Grace Hopper");
        appointment.setCustomerPhone("+15555550200");
        appointment.setAppointmentDate(date);
        appointment.setStartTime(startTime);
        appointment.setEndTime(startTime.plusMinutes(service.getDurationInMinutes()));
        appointment.setStatus(status);
        appointment.setNotes("Integration test appointment");
        return appointment;
    }
}
