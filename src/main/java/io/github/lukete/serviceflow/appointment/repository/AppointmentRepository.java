package io.github.lukete.serviceflow.appointment.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import io.github.lukete.serviceflow.appointment.domain.entity.Appointment;
import io.github.lukete.serviceflow.user.domain.entity.User;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
  List<Appointment> findByUser(User user);

  List<Appointment> findByAppointmentDate(LocalDate appointmentDate);

  List<Appointment> findByUserIdAndAppointmentDate(UUID userId, LocalDate appointmentDate);

  @Query("""
          select count(a) > 0
          from AppointmentEntity a
          where a.user.id = :userId
            and a.appointmentDate = :appointmentDate
            and a.status = 'SCHEDULED'
            and :startTime < a.endTime
            and :endTime > a.startTime
      """)
  boolean existsConflict(
      UUID userId,
      LocalDate appointmentDate,
      LocalTime startTime,
      LocalTime endTime);
}
