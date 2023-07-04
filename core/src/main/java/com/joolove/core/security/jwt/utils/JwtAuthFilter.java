package com.joolove.core.security.jwt.utils;

import com.joolove.core.security.service.RefreshTokenService;
import com.joolove.core.security.service.UserDetailsServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {

        String[] staticResourcePatterns = { "/static/**", "/css/**", "/js/**", "/images/**" };
        boolean isStaticResource = Arrays.stream(staticResourcePatterns)
                .anyMatch(pattern -> new AntPathMatcher().match(pattern, request.getServletPath()));

        if (isStaticResource) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            System.out.println("====================== JwtAuthFilter.doFilterInternal ======================");
            String jwt = jwtUtils.getJwtFromCookies(request);
            String jwtRefresh = jwtUtils.getJwtRefreshFromCookies(request);

            if (jwt != null && jwtUtils.validateJwtToken(jwt)
                    && jwtRefresh != null && refreshTokenService.findByToken(jwtRefresh) != null) {
                // 위의 부분에서 refreshToken 을 확인하는 과정이 결국 DB 에서 조회하는 과정이기 때문에 성능 저하 될 가능성이 있음
                // refreshToken 을 redis 에 저장하는 것을 고려해보자.
                // refreshToken 을 redis 에 저장하면, redis 에 저장된 refreshToken 을 확인하는 과정을 코드에 추가한다.

                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                buildAuthenticationUserDetails(userDetailsService.loadUserByUsername(username));
            }

        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
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