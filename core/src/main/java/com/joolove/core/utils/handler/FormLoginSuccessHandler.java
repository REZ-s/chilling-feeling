package com.joolove.core.utils.handler;

import com.joolove.core.utils.JwtUtils;
import com.joolove.core.service.RefreshTokenService;
import com.joolove.core.utils.oauth2.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class FormLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        ResponseCookie accessToken = jwtUtils.generateJwtCookie(userPrincipal.getUsername());
        ResponseCookie refreshToken = refreshTokenService.getRefreshTokenCookie(userPrincipal.getUsername());

        response.addHeader(HttpHeaders.SET_COOKIE, accessToken.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshToken.toString());

        response.sendRedirect("/");
    }
}
