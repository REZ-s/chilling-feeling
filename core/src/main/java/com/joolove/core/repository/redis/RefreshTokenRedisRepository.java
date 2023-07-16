package com.joolove.core.repository.redis;

import com.joolove.core.domain.auth.RefreshToken;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, UUID> {

    RefreshToken findByToken(String token);

    RefreshToken findByUsername(String username);

    void deleteByUsername(String username);

    void deleteByToken(String token);

    @CacheEvict(value = "refreshToken", allEntries = true)
    void deleteAll();

}
