package com.joolove.core.repository.redis;

import com.joolove.core.domain.auth.RefreshToken;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, UUID> {

    @Cacheable(value = "refreshToken", key = "#token", unless = "#result == null")
    RefreshToken findByToken(String token);

    @Cacheable(value = "refreshToken", key = "#username", unless = "#result == null")
    RefreshToken findByUsername(String username);

    @CacheEvict(value = "refreshToken", key = "#username")
    void deleteByUsername(String username);

    @CacheEvict(value = "refreshToken", key = "#token")
    void deleteByToken(String token);

    @CacheEvict(value = "refreshToken", allEntries = true)
    void deleteAll();

}
