package io.github.lukete.serviceflow.service.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.lukete.serviceflow.service.domain.entity.ServiceEntity;

public interface ServiceRepository extends JpaRepository<ServiceEntity, UUID> {
    Optional<List<ServiceEntity>> findByUserId(UUID userId);
}
