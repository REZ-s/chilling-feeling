package com.joolove.core.service;

import com.joolove.core.domain.auth.RefreshToken;
import com.joolove.core.repository.RefreshTokenRepository;
import com.joolove.core.utils.JwtUtils;
import com.joolove.core.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RefreshTokenService {
    private static final Logger logger = LoggerFactory.getLogger(RefreshTokenService.class);
    @Value("${joolove.app.jwtRefreshExpirationSecond}")
    private Long refreshTokenExpirationSecond;
    private final JwtUtils jwtUtils;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RedisUtils redisUtils;

    public RefreshToken findByToken(String token) {
        Object cachedToken = redisUtils.get(token, RefreshToken.class);
        if (cachedToken == null) {
            RefreshToken refreshToken = refreshTokenRepository.findByToken(token);
            if (refreshToken != null) {
                redisUtils.add(token, refreshToken, 14, TimeUnit.DAYS);
            }

            return refreshToken;
        }

        return (RefreshToken) cachedToken;
    }

    public RefreshToken findByUsername(String username) {
        Object cachedToken = redisUtils.get(username, RefreshToken.class);
        if (cachedToken == null) {
            RefreshToken refreshToken = refreshTokenRepository.findByUsername(username);
            if (refreshToken != null) {
                redisUtils.add(username, refreshToken, 14, TimeUnit.DAYS);
            }

            return refreshToken;
        }

        return (RefreshToken) cachedToken;
    }

    public ResponseCookie getRefreshTokenCookie(String username) {
        ResponseCookie refreshToken = null;
        RefreshToken token = findByUsername(username);

        if (token == null || !validateJwtRefresh(token.getToken())) {
            RefreshToken newToken = createRefreshToken(username);
            refreshToken = jwtUtils.generateRefreshJwtCookie(newToken.getToken());
        }
        else {
            refreshToken = jwtUtils.generateRefreshJwtCookie(token.getToken());
        }

        return refreshToken;
    }

    @Transactional
    public RefreshToken createRefreshToken(String username) {
        RefreshToken refreshToken = RefreshToken.builder()
                .username(username)
                .expiryDate(LocalDateTime.now().plusSeconds(refreshTokenExpirationSecond))
                .token(UUID.randomUUID().toString())
                .build();

        RefreshToken savedToken = refreshTokenRepository.save(refreshToken);
        redisUtils.add(username, savedToken, 14, TimeUnit.DAYS);
        redisUtils.add(savedToken.getToken(), savedToken, 14, TimeUnit.DAYS);
        return savedToken;
    }

    public boolean validateJwtRefresh(String jwtRefreshString) {
        if (jwtRefreshString == null) {
            return false;
        }

        RefreshToken refreshToken = findByToken(jwtRefreshString);
        if (refreshToken == null) {
            logger.info("Refresh token is null");
            return false;
        }

        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            logger.info("Refresh token was expired : {}", refreshToken.getToken());
            return false;
        }

        return true;
    }

    @Transactional
    public boolean deleteByUsernameAndToken(RefreshToken token) {
        return deleteByUsername(token.getUsername()) && deleteByToken(token.getToken());
    }

    @Transactional
    public boolean deleteByUsername(String username) {
        refreshTokenRepository.deleteByUsername(username);
        return redisUtils.remove(username, RefreshToken.class);
    }

    @Transactional
    public boolean deleteByToken(String token) {
        refreshTokenRepository.deleteByToken(token);
        return redisUtils.remove(token, RefreshToken.class);
    }

}