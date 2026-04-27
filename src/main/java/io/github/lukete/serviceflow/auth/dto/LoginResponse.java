package io.github.lukete.serviceflow.auth.dto;

public record LoginResponse(
        String accessToken,
        String tokenType) {

}
