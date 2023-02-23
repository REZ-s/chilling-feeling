package com.joolove.core.security.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joolove.core.domain.auth.RefreshToken;
import com.joolove.core.repository.UserRepository;
import com.joolove.core.security.jwt.repository.AuthenticationRepository;
import com.joolove.core.security.jwt.utils.JwtUtils;
import com.joolove.core.security.service.RefreshTokenService;
import com.joolove.core.security.service.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        super.onAuthenticationSuccess(request, response, authentication);   // clear temporary session & redirect

        // create & setting jwt
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        // access token
        String accessToken1 = jwtUtils.generateTokenFromUsername(userPrincipal.getUser().getUsername());
        String accessToken = jwtUtils.generateJwtCookie(userPrincipal).toString();
        System.out.println(accessToken1);   // 딱 토큰만 반환
        System.out.println(accessToken);   // 토큰 전체 정보를 반환

        // refresh token
        String refreshToken = "";
        Optional<RefreshToken> token = refreshTokenService.findByUser(userPrincipal.getUser());
        if (token.isEmpty()) {
            RefreshToken newToken = refreshTokenService.createRefreshToken(userPrincipal.getUser().getId());
            refreshToken = jwtUtils.generateRefreshJwtCookie(newToken.getToken()).toString();
        }
        else {
            RefreshToken originToken = token.get();
            refreshToken = jwtUtils.generateRefreshJwtCookie(originToken.getToken()).toString();
        }

        System.out.println(refreshToken);  // 토큰 전체 정보를 반환

        response.addHeader("Authorization", accessToken);
        response.addHeader("Refresh", refreshToken);
        response.setContentType("text/html;charset=UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(accessToken + refreshToken);
        response.getWriter().flush();
        //getRedirectStrategy().sendRedirect(request, response, "/auth/success");
    }

}