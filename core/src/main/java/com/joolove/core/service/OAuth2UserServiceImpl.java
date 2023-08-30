package com.joolove.core.service;

import com.joolove.core.domain.auth.Password;
import com.joolove.core.domain.auth.Role;
import com.joolove.core.domain.auth.SocialLogin;
import com.joolove.core.domain.member.User;
import com.joolove.core.repository.UserRepository;
import com.joolove.core.utils.PasswordUtils;
import com.joolove.core.utils.oauth2.OAuth2UserInfo;
import com.joolove.core.utils.oauth2.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {
    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(request);

        String provider = request.getClientRegistration().getRegistrationId();

        OAuth2UserInfo oAuth2UserInfo = UserPrincipal.buildOAuth2UserInfo(provider, oAuth2User.getAttributes());

        String providerId = oAuth2UserInfo.getProviderId();
        String username = oAuth2UserInfo.getEmail();

        User user = userService.joinByOAuth2(username, provider, providerId);

        return UserPrincipal.buildOAuth2User(user.getUsername(), user.getPassword().getPw(), oAuth2UserInfo);
    }
}

