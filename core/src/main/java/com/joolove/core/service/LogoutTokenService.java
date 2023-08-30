package com.joolove.core.service;

import com.joolove.core.domain.auth.LogoutToken;
import com.joolove.core.domain.auth.RefreshToken;
import com.joolove.core.repository.LogoutTokenRepository;
import com.joolove.core.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LogoutTokenService {
    private final LogoutTokenRepository logoutTokenRepository;
    private final RedisUtils redisUtils;

    public LogoutToken findByToken(String token) {
        Object cachedToken = redisUtils.get(token, LogoutToken.class);
        if (cachedToken == null) {
            LogoutToken logoutToken = logoutTokenRepository.findByToken(token);
            if (logoutToken != null) {
                redisUtils.add(token, logoutToken, 14, TimeUnit.DAYS);
            }

            return logoutToken;
        }

        return (LogoutToken) cachedToken;
    }

    @Transactional
    public LogoutToken createLogoutToken(RefreshToken token) {
        LogoutToken logoutToken = LogoutToken.builder()
                .username(token.getUsername())
                .token(token.getToken())
                .expiryDate(LogoutToken.setExpiryDate(token))
                .build();

        LogoutToken savedToken = logoutTokenRepository.save(logoutToken);
        redisUtils.add(savedToken.getToken(), savedToken, 14, TimeUnit.DAYS);
        return savedToken;
    }

    @Transactional
    public boolean deleteByToken(String token) {
        logoutTokenRepository.deleteByToken(token);
        return redisUtils.remove(token, LogoutToken.class);
    }
}
