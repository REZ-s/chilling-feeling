package com.joolove.core.security.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import com.joolove.core.domain.auth.RefreshToken;
import com.joolove.core.domain.member.User;
import com.joolove.core.security.jwt.exception.TokenRefreshException;
import com.joolove.core.security.jwt.repository.RefreshTokenRepository;
import com.joolove.core.repository.UserRepository;
import com.joolove.core.security.jwt.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    @Value("${joolove.app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    private final JwtUtils jwtUtils;

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepository userRepository;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public Optional<RefreshToken> findByUser(User user) {
        return refreshTokenRepository.findByUser(user);
    }

    public ResponseCookie getRefreshToken(UserPrincipal userPrincipal) {
        ResponseCookie refreshToken = null;
        Optional<RefreshToken> token = findByUser(userPrincipal.getUser());
        if (token.isEmpty()) {
            RefreshToken newToken = createRefreshToken(userPrincipal.getUser().getId());
            refreshToken = jwtUtils.generateRefreshJwtCookie(newToken.getToken());
        }
        else {
            RefreshToken originToken = verifyExpiration(token.get());
            refreshToken = jwtUtils.generateRefreshJwtCookie(originToken.getToken());
        }

        return refreshToken;
    }

    public RefreshToken createRefreshToken(UUID userId) {
        RefreshToken refreshToken = RefreshToken.builder()
                .user(userRepository.findById(userId).get())
                .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
                .token(UUID.randomUUID().toString())
                .build();
        refreshToken = refreshTokenRepository.save(refreshToken);

        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }

        return token;
    }

    public int deleteByUser(User user) {
        return refreshTokenRepository.deleteByUser(userRepository.findByUsername(user.getUsername()).get());
    }

    public int deleteByToken(String token) {
        return refreshTokenRepository.deleteByToken(token);
    }
}