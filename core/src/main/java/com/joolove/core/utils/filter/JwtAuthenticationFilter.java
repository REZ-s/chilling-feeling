package com.joolove.core.utils.filter;

import com.joolove.core.domain.auth.RefreshToken;
import com.joolove.core.service.LogoutTokenService;
import com.joolove.core.service.RefreshTokenService;
import com.joolove.core.service.UserDetailsServiceImpl;
import com.joolove.core.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.joolove.core.utils.StaticResourceUtils.isStaticResource;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;
    private final RefreshTokenService refreshTokenService;
    private final LogoutTokenService logoutTokenService;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        if (isStaticResource(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = jwtUtils.getJwtFromCookies(request);
        String jwtRefresh = jwtUtils.getJwtRefreshFromCookies(request);
        String username = null;

        if (jwtUtils.validateJwt(jwt)) {    // access token 이 유효한 경우
            username = jwtUtils.getUsernameFromJwtToken(jwt);
        } else if (refreshTokenService.validateJwtRefresh(jwtRefresh)) {    // refresh token 이 유효한 경우, 재발급
            // RTR (Refresh Token Rotation) 방식으로 토큰 재발급
            RefreshToken oldRefreshToken = refreshTokenService.findByToken(jwtRefresh);
            username = oldRefreshToken.getUsername();

            // 기존 토큰 폐기
            jwtUtils.deleteJwtCookies(request, response);
            refreshTokenService.deleteByUsernameAndToken(oldRefreshToken);
            logoutTokenService.createLogoutToken(oldRefreshToken);

            // 새 토큰 발급
            ResponseCookie newAccessToken = jwtUtils.generateJwtCookie(username);
            ResponseCookie newRefreshToken = refreshTokenService.getRefreshTokenCookie(username);

            response.addHeader(HttpHeaders.SET_COOKIE, newAccessToken.toString());
            response.addHeader(HttpHeaders.SET_COOKIE, newRefreshToken.toString());
        } else {    // refresh token 이 유효하지 않은 경우, 토큰 제거 후 로그아웃 처리
            if (jwtRefresh != null) {
                jwtUtils.deleteJwtCookies(request, response);
                RefreshToken oldRefreshToken = refreshTokenService.findByToken(jwtRefresh);

                if (oldRefreshToken != null) {
                    refreshTokenService.deleteByUsernameAndToken(oldRefreshToken);
                    logoutTokenService.createLogoutToken(oldRefreshToken);
                }
            }

            filterChain.doFilter(request, response);
            return;
        }

        buildAuthenticationUserDetails(userDetailsService.loadUserByUsername(username));
        filterChain.doFilter(request, response);
    }

    private void buildAuthenticationUserDetails(UserDetails userDetails) {
        if (userDetails == null) {
            return;
        }

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails.getUsername(),
                        userDetails.getPassword(),
                        userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}