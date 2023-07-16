package com.joolove.core.security.service;

import com.joolove.core.domain.auth.LogoutToken;
import com.joolove.core.domain.auth.RefreshToken;
import com.joolove.core.repository.jpa.LogoutTokenRepository;
import com.joolove.core.repository.redis.LogoutTokenRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LogoutTokenService {
    private final LogoutTokenRepository logoutTokenRepository;
    private final LogoutTokenRedisRepository logoutTokenRedisRepository;

    public LogoutToken findByToken(String token) {
        LogoutToken tokenCache = logoutTokenRedisRepository.findByToken(token);
        return tokenCache == null ? logoutTokenRepository.findByToken(token) : tokenCache;
    }

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

    public int deleteByUsername(String username) {
        return logoutTokenRepository.deleteByUsername(username);
    }

    public int deleteByToken(String token) {
        return logoutTokenRepository.deleteByToken(token);
    }
}
