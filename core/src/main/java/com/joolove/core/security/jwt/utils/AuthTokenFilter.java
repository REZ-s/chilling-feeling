package com.joolove.core.security.jwt.utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.joolove.core.security.service.OAuth2UserServiceImpl;
import com.joolove.core.security.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class AuthTokenFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            //String jwt = jwtUtils.getJwtFromCookies(request);
            String jwt = getJwtFromRequest(request);

            /*
            * getJwtFromCookies 로 Cookie 를 찾는 과정과
            * getJwtFromRequest 로 jwt 를 찾는 과정이 다른게 아닐까?
            * 예를들어서 둘다 jwt 목적으로 헤더에 세팅을 하지만, 세부 naming이 달라서 못찾는다던가
            * form login 을 통해 접근해왔을 때는 문제없이 잘찾았는데, 이는 jwtUtils 전체가 일단 form login 에 적합하게 먼저 만들었기때문
            * social login 을 통해 접근해왔을 때는 jwt 찾아도 null 이였는데, 이를 면밀히 살펴볼 필요성이 있겠다.
            * 그런데 jwt를 만들때 동일한 메소드를 사용해서 찾아야하지않나? 흠.....
            * 남은건 jwt를 어디에 붙였느냐 세팅했느냐에 따라 못찾을수도 있다고 본다.
            *
            * */

            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
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
                        userDetails,
                        null,
                        userDetails.getAuthorities());

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}