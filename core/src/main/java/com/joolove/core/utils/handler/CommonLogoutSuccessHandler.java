package com.joolove.core.utils.handler;

import com.joolove.core.domain.auth.RefreshToken;
import com.joolove.core.utils.JwtUtils;
import com.joolove.core.service.LogoutTokenService;
import com.joolove.core.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class CommonLogoutSuccessHandler implements LogoutHandler {
    private final RefreshTokenService refreshTokenService;
    private final LogoutTokenService logoutTokenService;
    private final JwtUtils jwtUtils;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String refreshToken = jwtUtils.getJwtRefreshFromCookies(request);
        RefreshToken token = refreshTokenService.findByToken(refreshToken);

        if (token != null) {
            refreshTokenService.deleteByUsernameAndToken(token);
            logoutTokenService.createLogoutToken(token);
        }
    }
}
