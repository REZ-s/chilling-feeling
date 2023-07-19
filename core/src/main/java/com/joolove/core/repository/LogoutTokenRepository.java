package com.joolove.core.repository;

import com.joolove.core.domain.auth.LogoutToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface LogoutTokenRepository extends JpaRepository<LogoutToken, UUID> {

    LogoutToken findByToken(String token);

    LogoutToken findByUsername(String username);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("delete from LogoutToken r where r.username = :username")
    int deleteByUsername(@Param("username") String username);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("delete from LogoutToken r where r.token = :token")
    int deleteByToken(@Param("token") String token);
}
