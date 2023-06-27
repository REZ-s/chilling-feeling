package com.joolove.core.repository;

import com.joolove.core.domain.auth.SocialLogin;
import com.joolove.core.domain.member.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SocialLoginRepository extends JpaRepository<SocialLogin, UUID> {

    boolean existsByUser(User user);
}
