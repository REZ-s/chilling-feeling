package com.joolove.core.security.oauth2;

import com.joolove.core.domain.auth.RefreshToken;
import com.joolove.core.domain.member.User;
import com.joolove.core.security.jwt.utils.JwtUtils;
import com.joolove.core.security.service.LogoutTokenService;
import com.joolove.core.security.service.RefreshTokenService;
import com.joolove.core.security.service.UserPrincipal;
import com.joolove.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CommonLogoutSuccessHandler implements LogoutHandler {

    private final RefreshTokenService refreshTokenService;
    private final LogoutTokenService logoutTokenService;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String refreshToken = jwtUtils.getJwtRefreshFromCookies(request);

        if (refreshTokenService.findByToken(refreshToken) != null) {
            RefreshToken token = refreshTokenService.findByToken(refreshToken);
            User user = token.getUser();
            logoutTokenService.createRefreshToken(user.getId());
            refreshTokenService.deleteByUser(user);
        }
    }
}
