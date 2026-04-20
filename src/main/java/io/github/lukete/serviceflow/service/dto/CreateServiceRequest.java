package io.github.lukete.serviceflow.service.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record CreateServiceRequest(
        @NotBlank(message = "name is required") String name,
        String description,
        @NotNull(message = "durationInMinutes is required") @Positive(message = "durationInMinutes must be greater than 0") Integer durationInMinutes,
        @NotNull(message = "price is required") @PositiveOrZero(message = "price must be greater than or equal to 0") BigDecimal price) {
}