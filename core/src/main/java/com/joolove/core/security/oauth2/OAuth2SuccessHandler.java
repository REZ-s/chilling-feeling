package com.joolove.core.security.oauth2;

import com.joolove.core.config.AppProperties;
import com.joolove.core.security.jwt.exception.BadRequestException;
import com.joolove.core.security.jwt.repository.HttpCookieOAuth2AuthorizationRequestRepository;
import com.joolove.core.security.jwt.utils.JwtUtils;
import com.joolove.core.security.service.RefreshTokenService;
import com.joolove.core.security.service.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;

import static com.joolove.core.security.jwt.repository.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;
    private final AppProperties appProperties;
    private final RefreshTokenService refreshTokenService;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        //여기에 Header Set cookie? 그럼 addCookie랑 addHeader랑 setHeader랑 차이를 알아야한다..


        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String redirectUri = jwtUtils.getCookieValueByName(request, REDIRECT_URI_PARAM_COOKIE_NAME);

        if (redirectUri != null && !isAuthorizedRedirectUri(redirectUri)) {
            throw new BadRequestException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
        }

        String targetUrl = (redirectUri == null ? getDefaultTargetUrl() : redirectUri);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        String accessToken = jwtUtils.generateJwtCookie(userPrincipal).toString();
        // 리프레시 토큰은 잠시 두자.
        String refreshToken = refreshTokenService.getRefreshToken(userPrincipal);

        return UriComponentsBuilder
                .fromUriString(targetUrl)
                .queryParam("token", accessToken)
                .build()
                .toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        return appProperties.getOauth2().getAuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                    // Only validate host and port. Let the clients use different paths if they want to
                    URI authorizedURI = URI.create(authorizedRedirectUri);
                    return authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedURI.getPort() == clientRedirectUri.getPort();
                });
    }
}