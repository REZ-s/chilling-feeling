package com.joolove.core.repository;

import com.joolove.core.domain.auth.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    RefreshToken findByToken(String token);

    RefreshToken findByUsername(String username);

    @Modifying(clearAutomatically = true)
    @Query("delete from RefreshToken r where r.username = :username")
    void deleteByUsername(@Param("username") String username);

    @Modifying(clearAutomatically = true)
    @Query("delete from RefreshToken r where r.token = :token")
    void deleteByToken(@Param("token") String token);
}