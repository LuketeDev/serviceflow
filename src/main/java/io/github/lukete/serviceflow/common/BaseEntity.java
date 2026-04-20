package io.github.lukete.serviceflow.common;

import java.time.LocalDateTime;

import jakarta.persistence.Column;

public class BaseEntity {
    @Column
    private LocalDateTime createdAt;
    @Column
    private LocalDateTime updatedAt;
}
