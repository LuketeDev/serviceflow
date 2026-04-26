package io.github.lukete.serviceflow.service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.lukete.serviceflow.service.dto.CreateServiceRequest;
import io.github.lukete.serviceflow.service.dto.ServiceResponse;
import io.github.lukete.serviceflow.service.dto.UpdateServiceRequest;
import io.github.lukete.serviceflow.service.service.ServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping(path = "/api/services")
@RequiredArgsConstructor
public class ServiceController {
    private final ServiceService serviceService;

    @PostMapping()
    public ResponseEntity<ServiceResponse> createService(@Valid @RequestBody CreateServiceRequest request) {
        ServiceResponse response = serviceService.createService(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public List<ServiceResponse> getAll(
            @RequestParam(required = false) UUID userId) {

        if (userId != null) {
            return serviceService.findByUserId(userId);
        }

        return serviceService.findAll();
    }

    @GetMapping("/{id}")
    public ServiceResponse getById(@PathVariable UUID id) {
        return serviceService.findById(id);
    }

    @PutMapping("/{id}")
    public ServiceResponse update(@PathVariable UUID id, @Valid @RequestBody UpdateServiceRequest request) {
        return serviceService.update(id, request);
    }

    @PatchMapping("/{id}")
    public ServiceResponse deactivate(@PathVariable UUID id) {
        return serviceService.deactivate(id);
    }

}