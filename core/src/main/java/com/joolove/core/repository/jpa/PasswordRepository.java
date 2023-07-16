package com.joolove.core.repository.jpa;

import com.joolove.core.domain.auth.Password;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PasswordRepository extends JpaRepository<Password, UUID> {

    Boolean existsByPw(String pw);
}