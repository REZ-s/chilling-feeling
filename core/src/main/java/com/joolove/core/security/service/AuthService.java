package com.joolove.core.security.service;

import com.joolove.core.security.jwt.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final RefreshTokenService refreshTokenService;

    private final JwtUtils jwtUtils;

    public ResponseEntity<?> logout(boolean isCorrectPath) {
        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!Objects.equals(principle.toString(), "anonymousUser")) {
            UUID userId = ((UserPrincipal) principle).getUser().getId();
            refreshTokenService.deleteByUserId(userId);
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtUtils.getCleanJwtCookie().toString())
                .header(HttpHeaders.SET_COOKIE, jwtUtils.getCleanJwtRefreshCookie().toString())
                .body("You've been signed out! " + (isCorrectPath ? "(correct path)" : "(access token does not matched)"));
    }
}
