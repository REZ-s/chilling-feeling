package com.joolove.core.security.service;

import com.joolove.core.domain.auth.LogoutToken;
import com.joolove.core.domain.auth.RefreshToken;
import com.joolove.core.repository.jpa.LogoutTokenRepository;
import com.joolove.core.repository.redis.LogoutTokenRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LogoutTokenService {
    private final LogoutTokenRepository logoutTokenRepository;
    private final LogoutTokenRedisRepository logoutTokenRedisRepository;

    @Cacheable(value = "logoutToken", key = "#token", unless = "#token == null or #result == null")
    public LogoutToken findByToken(String token) {
        LogoutToken tokenCache = logoutTokenRedisRepository.findByToken(token);
        return tokenCache == null ? logoutTokenRepository.findByToken(token) : tokenCache;
    }

    @Cacheable(value = "logoutToken", key = "#username", unless = "#username == null or #result == null")
    public LogoutToken findByUsername(String username) {
        LogoutToken tokenCache = logoutTokenRedisRepository.findByUsername(username);
        return tokenCache == null ? logoutTokenRepository.findByUsername(username) : tokenCache;
    }

    public LogoutToken createLogoutToken(RefreshToken token, String username) {
        LogoutToken logoutToken = LogoutToken.builder()
                .username(username)
                .token(UUID.randomUUID().toString())
                .expiryDate(LogoutToken.setExpiryDate(token))
                .build();

        logoutTokenRedisRepository.save(logoutToken);
        return logoutTokenRepository.save(logoutToken);
    }

    @CacheEvict(value = "logoutToken", key = "#username", condition = "#username != null")
    public int deleteByUsername(String username) {
        logoutTokenRedisRepository.deleteByUsername(username);
        return logoutTokenRepository.deleteByUsername(username);
    }

    @CacheEvict(value = "logoutToken", key = "#token", condition = "#token != null")
    public int deleteByToken(String token) {
        logoutTokenRedisRepository.deleteByToken(token);
        return logoutTokenRepository.deleteByToken(token);
    }
}
