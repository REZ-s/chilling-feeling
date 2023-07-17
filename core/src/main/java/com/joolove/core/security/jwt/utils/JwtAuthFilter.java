package com.joolove.core.security.jwt.utils;

import com.joolove.core.domain.auth.RefreshToken;
import com.joolove.core.security.service.LogoutTokenService;
import com.joolove.core.security.service.RefreshTokenService;
import com.joolove.core.security.service.UserDetailsServiceImpl;
import com.joolove.core.security.service.UserPrincipal;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

public class JwtAuthFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private LogoutTokenService logoutTokenService;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {

        // 정적 리소스 필터링
        String[] staticResourcePatterns = {"/css/**", "/js/**", "/images/**"};
        boolean isStaticResource = Arrays.stream(staticResourcePatterns)
                .anyMatch(pattern -> new AntPathMatcher().match(pattern, request.getServletPath()));

        if (isStaticResource) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String jwt = jwtUtils.getJwtFromCookies(request);
            String jwtRefresh = jwtUtils.getJwtRefreshFromCookies(request);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = null;

            if (jwtRefresh != null
                    && refreshTokenService.findByToken(jwtRefresh) != null
                    && refreshTokenService.verifyExpiration(refreshTokenService.findByToken(jwtRefresh))) {    // refresh token 이 유효한 경우

                if (jwt != null && jwtUtils.validateJwtToken(jwt)) {    // access token 이 유효한 경우
                    username = jwtUtils.getUsernameFromJwtToken(jwt);
                } else {                                                // access token 이 유효하지 않은 경우
                    username = refreshTokenService.findByToken(jwtRefresh).getUsername();

                    // RTR (Refresh Token Rotation) 방식으로 토큰 재발급
                    if (authentication != null && authentication.getPrincipal() != null) {
                        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
                        RefreshToken oldRefreshToken = refreshTokenService.findByToken(jwtRefresh);

                        // 기존 토큰 폐기
                        jwtUtils.deleteJwtCookies(request, response);
                        logoutTokenService.createLogoutToken(oldRefreshToken, oldRefreshToken.getUsername());
                        refreshTokenService.deleteByUsername(oldRefreshToken.getUsername());

                        // 새 토큰 발급
                        ResponseCookie newAccessToken = jwtUtils.generateJwtCookie(userPrincipal);
                        ResponseCookie newRefreshToken = refreshTokenService.getRefreshTokenCookie(userPrincipal);

                        response.addHeader(HttpHeaders.SET_COOKIE, newAccessToken.toString());
                        response.addHeader(HttpHeaders.SET_COOKIE, newRefreshToken.toString());
                    }
                }

                if (authentication == null) {   // 이전 과정에서 설정한 정보가 있으면 다시 설정하지 않는다.
                    buildAuthenticationUserDetails(userDetailsService.loadUserByUsername(username));
                }
            } else {
                // refresh token 이 유효하지 않은 경우, 토큰 제거 후 로그아웃 처리
                jwtUtils.deleteJwtCookies(request, response);

                if (jwtRefresh != null) {
                    RefreshToken oldRefreshToken = refreshTokenService.findByToken(jwtRefresh);

                    if (oldRefreshToken != null) {
                        logoutTokenService.createLogoutToken(oldRefreshToken, oldRefreshToken.getUsername());
                        refreshTokenService.deleteByUsername(oldRefreshToken.getUsername());
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: ", e);
        }

        filterChain.doFilter(request, response);
    }

    private void buildAuthenticationUserDetails(@NotNull UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails.getUsername(),
                        null,
                        userDetails.getAuthorities());

        // 아래 코드는 username, password 외에도 다른 정보로 인증을 할 때 사용한다.
        // 인증 부가기능이라고 하는데, 현재 프로젝트에서 필요하지 않으므로 세팅하지 않는다.
        // authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}