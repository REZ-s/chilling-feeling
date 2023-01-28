package com.joolove.core.security.jwt.repository;

import com.joolove.core.domain.auth.Authentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuthenticationRepository extends JpaRepository<Authentication, UUID> {
    Boolean existsByEmail(String email);
}
