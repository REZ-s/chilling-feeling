package com.joolove.core.security.jwt.utils;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.joolove.core.security.service.LogoutTokenService;
import com.joolove.core.security.service.RefreshTokenService;
import com.joolove.core.security.service.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private RefreshTokenService refreshTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = jwtUtils.getJwtFromCookies(request);
            String jwtRefresh = jwtUtils.getJwtRefreshFromCookies(request);

            if (jwt != null && jwtUtils.validateJwtToken(jwt)
                    && jwtRefresh != null && refreshTokenService.findByToken(jwtRefresh).isPresent()) {
                // 위의 부분에서 refrshToken을 확인하는 과정이 결국 DB에서 조회하는 과정이기 때문에 성능상의 문제가 있을 수 있음
                // 해결책은 refresh token을 redis에 저장하는 것을 고려해보자
                // refresh token을 redis에 저장하면, redis에 저장된 refresh token을 확인하는 과정이 필요함

                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                buildAuthenticationUserDetails(request, userDetailsService.loadUserByUsername(username));
            }

        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }

        filterChain.doFilter(request, response);
    }

    private void buildAuthenticationUserDetails(HttpServletRequest request, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails.getUsername(),
                        userDetails.getPassword(),
                        userDetails.getAuthorities());

        // 아래 코드가 꼭 필요할지 확인
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}