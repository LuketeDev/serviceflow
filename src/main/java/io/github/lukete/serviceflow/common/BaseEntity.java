package io.github.lukete.serviceflow.common;

import java.time.LocalDateTime;

import jakarta.persistence.Column;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class BaseEntity {
    @Column
    private LocalDateTime createdAt;
    @Column
    private LocalDateTime updatedAt;
}
