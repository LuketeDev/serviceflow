package io.github.lukete.serviceflow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // Allows doing things w/o token
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()) // Allows all endpoints w/o auth
                .httpBasic(Customizer.withDefaults()) // Default header user + pass auth
                .build();
    }
}
