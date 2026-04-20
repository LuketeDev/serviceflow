package io.github.lukete.serviceflow.exceptions;

import java.time.LocalDateTime;

public record ApiError(
        LocalDateTime timestamp,
        int status,
        String message,
        String path) {

}
