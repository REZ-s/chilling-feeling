package com.joolove.core.security.service;

import com.joolove.core.domain.ERole;
import com.joolove.core.domain.auth.Password;
import com.joolove.core.domain.auth.SocialLogin;
import com.joolove.core.domain.member.User;
import com.joolove.core.domain.member.UserRole;
import com.joolove.core.repository.RoleRepository;
import com.joolove.core.repository.SocialLoginRepository;
import com.joolove.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(request);

        String provider = request.getClientRegistration().getRegistrationId();
        String providerId = oAuth2User.getAttribute("sub");
        String username = provider + "_" + providerId;
        String email = oAuth2User.getAttribute("email");

        User user = createUserByOAuth2(username, email, provider, providerId);

        return UserPrincipal.buildOAuth2User(user, oAuth2User.getAttributes());

/*      String userNameAttributeName = request.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuth2Attribute oAuth2Attribute =
                OAuth2Attribute.of(provider, userNameAttributeName, oAuth2User.getAttributes());

        var memberAttribute = oAuth2Attribute.convertToMap();

        // OAuth2User 를 구현한 OAuth2UserImpl 같은 클래스를 따로 작성하진 않았다. 이미 제공하는 DefaultOAuth2User 로 충분하기 때문이다.
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                memberAttribute,
                "email");   // email 이 공통으로 사용되며, id를 식별할 수 있는 수단이므로 key 로 사용
*/
    }

    public User createUserByOAuth2(String username, String email, String provider, String providerId) {
        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            user = User.builder()
                    .username(username)
                    .accountType((short) 1)
                    .build();

            com.joolove.core.domain.auth.Authentication authentication = com.joolove.core.domain.auth.Authentication.builder()
                    .user(user)
                    .email(email)
                    .gatherAgree(true)
                    .build();
            user.setAuthentication(authentication);

            //여기서 있는 이 비밀번호를 출력해서 내가 직접 로그인 해볼까?
            String pw = UUID.randomUUID().toString();
            System.out.println("asdsad : " + pw);

            Password password = Password.builder()
                    .user(user)
                    .pw(passwordEncoder.encode(pw))
                    .build();
            user.setPassword(password);

            List<UserRole> userRoles = new ArrayList<>();
            UserRole userRole = UserRole.builder()
                    .user(user)
                    .role(roleRepository.findByName(ERole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found.")))
                    .build();
            userRoles.add(userRole);
            user.setRoles(userRoles);

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

