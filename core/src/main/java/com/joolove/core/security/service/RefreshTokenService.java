package com.joolove.core.security.service;

import com.joolove.core.domain.auth.RefreshToken;
import com.joolove.core.domain.member.User;
import com.joolove.core.repository.jpa.RefreshTokenRepository;
import com.joolove.core.repository.redis.RefreshTokenRedisRepository;
import com.joolove.core.security.jwt.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private static final Logger logger = LoggerFactory.getLogger(RefreshTokenService.class);
    @Value("${joolove.app.jwtRefreshExpirationSecond}")
    private Long refreshTokenExpirationSecond;
    private final JwtUtils jwtUtils;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    @Cacheable(value = "refreshToken", key = "#token", unless = "#token == null or #result == null")
    public RefreshToken findByToken(String token) {
        RefreshToken tokenCache = refreshTokenRedisRepository.findByToken(token);
        return tokenCache == null ? refreshTokenRepository.findByToken(token) : tokenCache;
    }

    @Cacheable(value = "refreshToken", key = "#username", unless = "#username == null or #result == null")
    public RefreshToken findByUsername(String username) {
        RefreshToken tokenCache = refreshTokenRedisRepository.findByUsername(username);
        return tokenCache == null ? refreshTokenRepository.findByUsername(username) : tokenCache;
    }

    public ResponseCookie getRefreshTokenCookie(UserPrincipal userPrincipal) {
        ResponseCookie refreshToken = null;
        RefreshToken token = findByUsername(userPrincipal.getUser().getUsername());

        if (token == null || !verifyExpiration(token)) {
            RefreshToken newToken = createRefreshToken(userPrincipal.getUser());
            refreshToken = jwtUtils.generateRefreshJwtCookie(newToken.getToken());
        }
        else {
            refreshToken = jwtUtils.generateRefreshJwtCookie(token.getToken());
        }

        return refreshToken;
    }

    public RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = RefreshToken.builder()
                .username(user.getUsername())
                .expiryDate(LocalDateTime.now().plusSeconds(refreshTokenExpirationSecond))
                .token(UUID.randomUUID().toString())
                .build();

        refreshTokenRedisRepository.save(refreshToken);
        return refreshTokenRepository.save(refreshToken);
    }

    public boolean verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            logger.info(token.getToken(), "Refresh token was expired. Please make a new signin request");
            return false;
        }

        return true;
    }

    @CacheEvict(value = "refreshToken", key = "#username", condition = "#username != null")
    public int deleteByUsername(String username) {
        refreshTokenRedisRepository.deleteByUsername(username);
        return refreshTokenRepository.deleteByUsername(username);
    }

    @CacheEvict(value = "refreshToken", key = "#token", condition = "#token != null")
    public int deleteByToken(String token) {
        refreshTokenRedisRepository.deleteByToken(token);
        return refreshTokenRepository.deleteByToken(token);
    }

}