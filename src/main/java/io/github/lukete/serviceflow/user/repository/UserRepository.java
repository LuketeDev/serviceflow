package io.github.lukete.serviceflow.user.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.lukete.serviceflow.user.domain.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {

}
