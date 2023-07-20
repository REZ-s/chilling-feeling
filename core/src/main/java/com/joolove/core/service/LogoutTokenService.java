package com.joolove.core.service;

import com.joolove.core.domain.auth.LogoutToken;
import com.joolove.core.domain.auth.RefreshToken;
import com.joolove.core.repository.LogoutTokenRepository;
import com.joolove.core.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutTokenService {
    private final LogoutTokenRepository logoutTokenRepository;
    private final RedisUtils redisUtils;

    public LogoutToken findByToken(String token) {
        Object cachedToken = redisUtils.get(token, LogoutToken.class);
        if (cachedToken == null) {
            LogoutToken logoutToken = logoutTokenRepository.findByToken(token);
            if (logoutToken != null) {
                redisUtils.add(token, logoutToken);
            }

            return logoutToken;
        }

        return (LogoutToken) cachedToken;
    }

    public LogoutToken createLogoutToken(RefreshToken token) {
        LogoutToken logoutToken = LogoutToken.builder()
                .username(token.getUsername())
                .token(token.getToken())
                .expiryDate(LogoutToken.setExpiryDate(token))
                .build();

        LogoutToken savedToken = logoutTokenRepository.save(logoutToken);
        redisUtils.add(savedToken.getToken(), savedToken);
        return savedToken;
    }

    public boolean deleteByToken(String token) {
        logoutTokenRepository.deleteByToken(token);
        return redisUtils.remove(token, LogoutToken.class);
    }
}
