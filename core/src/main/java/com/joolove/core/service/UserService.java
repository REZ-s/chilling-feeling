package com.joolove.core.service;

import com.joolove.core.domain.auth.Password;
import com.joolove.core.domain.auth.Role;
import com.joolove.core.domain.auth.SocialLogin;
import com.joolove.core.domain.member.User;
import com.joolove.core.dto.request.SignUpRequest;
import com.joolove.core.repository.UserRepository;
import com.joolove.core.utils.PasswordUtils;
import com.joolove.core.utils.RedisUtils;
import com.joolove.core.utils.oauth2.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordUtils passwordUtils;
    private final RedisUtils redisUtils;

    // 사용자 회원 가입 (Form)
    @Transactional
    public User joinByForm(SignUpRequest request) {
        User user = findByUsername(request.getUsername());

        if (user != null) {
            return user;
        }

        user = User.builder()
                .username(request.getUsername())
                .accountType((short) 2)
                .build();

        Password password = Password.builder()
                .user(user)
                .pw(passwordUtils.encode(request.getPassword()))
                .build();
        user.setPassword(password);

        Role role = Role.builder()
                .user(user)
                .name(Role.ERole.ROLE_USER)
                .build();
        user.setRole(role);

        return userRepository.save(user);
    }

    // 사용자 회원 가입 (OAuth2)
    @Transactional
    public User joinByOAuth2(String username, String provider, String providerId) {
        User user = findByUsername(username);

        if (user != null) {
            return user;
        }

        user = User.builder()
                .username(username)
                .accountType((short) 1)
                .build();

        Password password = Password.builder()
                .user(user)
                .pw(passwordUtils.encode(UUID.randomUUID().toString()))
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

        return userRepository.save(user);
    }

    // 사용자 회원 탈퇴
    @Transactional
    public User leave(User user) {
        userRepository.delete(user);
        return user;
    }

    // 사용자 정보 수정
    @Transactional
    public User update(User user) {
        User beforeUser = userRepository.findByUsername(user.getUsername());
        if (beforeUser == null) {
            return null;
        }

        beforeUser.updateAll(user);
        return userRepository.save(beforeUser);
    }

    // 사용자 계정과 비밀번호 조회 (인증용)
    public Map<String, String> getUsernamePasswordByUsernameForSecurityFilter(String username) {
        User user = findByUsername(username);
        if (user == null) {
            return null;
        }

        return Map.of("username", user.getUsername(), "password", user.getPassword().getPw());
    }

    // 사용자 조회
    public User findByUsername(String username) {
        Object cachedUserObject = redisUtils.get(username, User.class);
        if (cachedUserObject == null) {
            User user = userRepository.findByUsernameWithRelations(username);
            if (user != null) {
                redisUtils.add(user.getUsername(), user, 14, TimeUnit.DAYS);
            }

            return user;
        }

        return (User) cachedUserObject;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public String getUsernameByAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return "Guest";
        }

        Object principal = authentication.getPrincipal();
        if (principal == null) {
            return "Guest";
        }

        String username = (String) principal;
        if (username.equals("anonymousUser")) {
            return "Guest";
        }

        return username;
    }
}
