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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
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

    RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        HttpStatus targetStatus = HttpStatus.TEMPORARY_REDIRECT;
        String targetUrl = request.getRequestURI();
        System.out.println(targetUrl);
        String queryString = Optional.ofNullable(request.getQueryString()).orElseGet(String::new);

        if (response.isCommitted()) {
            super.logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
        } else {
            response.setStatus(targetStatus.value());
            response.setHeader("Location", targetUrl + "?" + queryString);
        }
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        // (1) 302 default redirect GET Method
        //super.onAuthenticationSuccess(request, response, authentication);   // clear temporary session & redirect

        // (2) 302 -> 307 re-define redirect (others Method)
        //this.handle(request, response, authentication);
        //super.clearAuthenticationAttributes(request);

        //위의 주석은 redirect 시 response 를 입맛대로 보낼 수 있을지에 대한 흔적

        // setting redirect address
        getRedirectStrategy().sendRedirect(request, response, "/loginForm");

        // create & setting jwt
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        // access token
        String accessToken = jwtUtils.generateJwtCookie(userPrincipal).toString();

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

        response.setHeader(HttpHeaders.SET_COOKIE, accessToken);
        response.setHeader(HttpHeaders.SET_COOKIE, refreshToken);
        response.setContentType("text/html;charset=UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(accessToken + refreshToken);
        response.getWriter().flush();
    }

}