package io.github.lukete.serviceflow.service.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record CreateServiceRequest(
        @NotBlank String name,
        String description,
        @NotNull @Positive Integer durationInMinutes,
        @NotNull @PositiveOrZero BigDecimal price) {

}
