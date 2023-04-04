package com.joolove.core.security.oauth2;

import com.joolove.core.domain.member.User;
import com.joolove.core.security.service.LogoutTokenService;
import com.joolove.core.security.service.RefreshTokenService;
import com.joolove.core.security.service.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CommonLogoutSuccessHandler implements LogoutHandler {

    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private LogoutTokenService logoutTokenService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        // authentication 객체가 null이면 로그인이 안된 상태라고함
        // 그럼 logout 처리가 되면서 이 handler 보다 빨리 처리가 되어 null 으로 바뀐것으로 예상.
        // 해결방법은 더 빠른 filter나 handler 에서 아래 과정을 처리하거나 request로 온 header 에서 jwt 쿠키 남아있으면 이걸로 해결하기


        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userPrincipal.getUser();

        logoutTokenService.createRefreshToken(user.getId());
        refreshTokenService.deleteByUser(user);

        SecurityContextHolder.getContext().setAuthentication(null);
        SecurityContextHolder.clearContext();

        try {
            response.sendRedirect("/");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
