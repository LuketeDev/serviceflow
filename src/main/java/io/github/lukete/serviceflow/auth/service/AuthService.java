package io.github.lukete.serviceflow.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.github.lukete.serviceflow.auth.dto.LoginRequest;
import io.github.lukete.serviceflow.auth.dto.LoginResponse;
import io.github.lukete.serviceflow.exceptions.InvalidCredentialsException;
import io.github.lukete.serviceflow.exceptions.ResourceNotFoundException;
import io.github.lukete.serviceflow.user.domain.entity.User;
import io.github.lukete.serviceflow.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private static final String USER_NOT_FOUND = "User not found with email: ";
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND + request.email()));

        if (!passwordEncoder.matches(user.getPassword(), request.password())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String token = jwtService.generateToken(user);

        return new LoginResponse(token, "Bearer");
    }
}
