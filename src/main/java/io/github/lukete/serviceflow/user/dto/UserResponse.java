package io.github.lukete.serviceflow.user.dto;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String email,
        String phone,
        boolean active) {
}