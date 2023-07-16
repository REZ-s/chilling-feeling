package com.joolove.core.repository.redis;

import com.joolove.core.domain.auth.LogoutToken;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LogoutTokenRedisRepository extends CrudRepository<LogoutToken, UUID> {

    @Cacheable(value = "logoutToken", key = "#token", unless = "#result == null")
    LogoutToken findByToken(String token);

    @Cacheable(value = "logoutToken", key = "#username", unless = "#result == null")
    LogoutToken findByUsername(String username);

    @CacheEvict(value = "logoutToken", key = "#username")
    void deleteByUsername(String username);

    @CacheEvict(value = "logoutToken", key = "#token")
    void deleteByToken(String token);

    @CacheEvict(value = "logoutToken", allEntries = true)
    void deleteAll();

}
