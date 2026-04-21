package io.github.lukete.serviceflow.service.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import io.github.lukete.serviceflow.exceptions.ResourceNotFoundException;
import io.github.lukete.serviceflow.service.domain.entity.ServiceEntity;
import io.github.lukete.serviceflow.service.dto.CreateServiceRequest;
import io.github.lukete.serviceflow.service.dto.ServiceResponse;
import io.github.lukete.serviceflow.service.dto.UpdateServiceRequest;
import io.github.lukete.serviceflow.service.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServiceService {
    private static final String NOT_FOUND = "Service not found with id: ";
    private final ServiceRepository serviceRepository;

    public ServiceResponse createService(CreateServiceRequest request) {
        // TO-DO add non-duplication

        ServiceEntity service = new ServiceEntity();
        service.setName(request.name());
        service.setDescription(request.description());
        service.setDurationInMinutes(request.durationInMinutes());
        service.setPrice(request.price());
        service.setActive(true);

        ServiceEntity saved = serviceRepository.save(service);
        return toResponse(saved);
    }

    public List<ServiceResponse> findAll() {
        return serviceRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public ServiceResponse findById(UUID id) {
        ServiceEntity service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND + id));
        return toResponse(service);
    }

    public ServiceResponse update(UUID id, UpdateServiceRequest request) {
        ServiceEntity service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND + id));

        service.setName(request.name());
        service.setDescription(request.description());
        service.setDurationInMinutes(request.durationInMinutes());
        service.setPrice(request.price());

        ServiceEntity updated = serviceRepository.save(service);
        return toResponse(updated);
    }

    public ServiceResponse deactivate(UUID id) {
        ServiceEntity service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND + id));

        service.setActive(false);

        ServiceEntity updated = serviceRepository.save(service);
        return toResponse(updated);
    }

    private ServiceResponse toResponse(ServiceEntity saved) {
        return new ServiceResponse(
                saved.getId(),
                saved.getName(),
                saved.getDescription(),
                saved.getDurationInMinutes(),
                saved.getPrice(),
                saved.isActive());
    }
}
