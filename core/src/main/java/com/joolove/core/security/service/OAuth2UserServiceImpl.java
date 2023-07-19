package com.joolove.core.security.service;

import com.joolove.core.domain.auth.Password;
import com.joolove.core.domain.auth.Role;
import com.joolove.core.domain.auth.SocialLogin;
import com.joolove.core.domain.member.User;
import com.joolove.core.repository.UserRepository;
import com.joolove.core.security.oauth2.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(request);

        String provider = request.getClientRegistration().getRegistrationId();

        OAuth2UserInfo oAuth2UserInfo = UserPrincipal.buildOAuth2UserInfo(provider, oAuth2User.getAttributes());

        String providerId = oAuth2UserInfo.getProviderId();
        String username = oAuth2UserInfo.getEmail();

        createUserByOAuth2(username, provider, providerId);

        return UserPrincipal.buildOAuth2User(username, oAuth2UserInfo);
    }

    public User createUserByOAuth2(String username, String provider, String providerId) {
        User user = userRepository.findByUsernameWithRelations(username);

        if (user == null) {
            user = User.builder()
                    .username(username)
                    .accountType((short) 1)
                    .build();

            Password password = Password.builder()
                    .user(user)
                    .pw(passwordEncoder.encode(UUID.randomUUID().toString()))
                    .build();
            user.setPassword(password);

            Role role = Role.builder()
                    .name(Role.ERole.ROLE_USER)
                    .user(user)
                    .build();
            user.setRole(role);

            SocialLogin socialLogin = SocialLogin.builder()
                    .user(user)
                    .providerId(providerId)
                    .providerCode(SocialLogin.convertToProviderCode(provider))
                    .build();
            user.setSocialLogin(socialLogin);

            userRepository.save(user);
        }

        return user;
    }
}

