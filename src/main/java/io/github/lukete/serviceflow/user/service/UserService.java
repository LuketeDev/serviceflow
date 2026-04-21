package io.github.lukete.serviceflow.user.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import io.github.lukete.serviceflow.exceptions.DuplicateResourceException;
import io.github.lukete.serviceflow.exceptions.ResourceNotFoundException;
import io.github.lukete.serviceflow.user.domain.entity.User;
import io.github.lukete.serviceflow.user.dto.CreateUserRequest;
import io.github.lukete.serviceflow.user.dto.UpdateUserRequest;
import io.github.lukete.serviceflow.user.dto.UserResponse;
import io.github.lukete.serviceflow.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final String NOT_FOUND = "User not found with id: ";
    private static final String DUPLICATE = "User already exists with the email: ";
    private final UserRepository userRepository;

    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException(DUPLICATE + request.email());
        }

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPhone(request.phone());
        user.setPassword(request.password());
        user.setActive(true);

        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User saved = userRepository.save(user);
        return toResponse(saved);
    }

    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public UserResponse findById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND + id));
        return toResponse(user);
    }

    public UserResponse update(UUID id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND + id));

        user.setName(request.name());
        user.setEmail(request.email());
        user.setPhone(request.phone());
        user.setPassword(request.password());
        user.setActive(true);

        user.setUpdatedAt(LocalDateTime.now());

        User updated = userRepository.save(user);
        return toResponse(updated);
    }

    public UserResponse deactivate(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND + id));

        user.setActive(false);
        user.setUpdatedAt(LocalDateTime.now());

        User updated = userRepository.save(user);
        return toResponse(updated);
    }

    private UserResponse toResponse(User saved) {
        return new UserResponse(
                saved.getId(),
                saved.getName(),
                saved.getEmail(),
                saved.getPhone(),
                saved.isActive());
    }
}
