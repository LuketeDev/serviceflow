package io.github.lukete.serviceflow.service.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ServiceResponse(
        UUID id,
        String name,
        String description,
        Integer durationInMinutes,
        BigDecimal price,
        boolean active) {
}
