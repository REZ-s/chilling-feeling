package com.joolove.core.security.jwt.repository;

import com.joolove.core.domain.auth.RefreshToken;
import com.joolove.core.domain.member.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    RefreshToken findByToken(String token);

    RefreshToken findByUser(User user);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("delete from RefreshToken r where r.user = :user")
    int deleteByUser(@Param("user") User user);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("delete from RefreshToken r where r.token = :token")
    int deleteByToken(@Param("token") String token);
}