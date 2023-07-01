package com.joolove.core.security.service;

import com.joolove.core.domain.auth.LogoutToken;
import com.joolove.core.domain.auth.RefreshToken;
import com.joolove.core.domain.member.User;
import com.joolove.core.repository.UserRepository;
import com.joolove.core.security.jwt.exception.TokenRefreshException;
import com.joolove.core.security.jwt.repository.LogoutTokenRepository;
import com.joolove.core.security.jwt.repository.RefreshTokenRepository;
import com.joolove.core.security.jwt.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LogoutTokenService {
    private final JwtUtils jwtUtils;

    private final LogoutTokenRepository logoutTokenRepository;

    private final UserRepository userRepository;

    public Optional<LogoutToken> findByToken(String token) {
        return logoutTokenRepository.findByToken(token);
    }

    public Optional<LogoutToken> findByUser(User user) {
        return logoutTokenRepository.findByUser(user);
    }

    public LogoutToken createRefreshToken(UUID userId) {
        LogoutToken logoutToken = LogoutToken.builder()
                .user(userRepository.findById(userId).get())
                .token(UUID.randomUUID().toString())
                .build();
        return logoutTokenRepository.save(logoutToken);
    }

    public int deleteByUser(User user) {
        return logoutTokenRepository.deleteByUser(userRepository.findByUsername(user.getUsername()));
    }

    public int deleteByToken(String token) {
        return logoutTokenRepository.deleteByToken(token);
    }
}
