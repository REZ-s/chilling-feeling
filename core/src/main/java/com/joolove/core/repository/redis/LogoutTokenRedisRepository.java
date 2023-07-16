package com.joolove.core.repository.redis;

import com.joolove.core.domain.auth.LogoutToken;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LogoutTokenRedisRepository extends CrudRepository<LogoutToken, UUID> {

    LogoutToken findByToken(String token);

    LogoutToken findByUsername(String username);

    void deleteByUsername(String username);

    void deleteByToken(String token);

    @CacheEvict(value = "logoutToken", allEntries = true)
    void deleteAll();

}
