package com.joolove.core.service;

import com.joolove.core.domain.auth.Password;
import com.joolove.core.domain.auth.Role;
import com.joolove.core.domain.member.User;
import com.joolove.core.dto.request.SignUpRequest;
import com.joolove.core.repository.UserRepository;
import com.joolove.core.utils.PasswordUtils;
import com.joolove.core.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordUtils passwordUtils;
    private final RedisUtils redisUtils;

    // 사용자 회원 가입
    @Transactional
    public User join(SignUpRequest request) {
        User user = User.builder()
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

    // 사용자 이름 조회 (인증용)
    public String findByUsernameSimple(String username) {
        Object cachedUsername = redisUtils.get(username, String.class);
        if (cachedUsername == null) {
            String simpleUsername = userRepository.findByUsernameSimple(username);
            if (simpleUsername != null) {
                redisUtils.add(simpleUsername, simpleUsername, 14, TimeUnit.DAYS);
            }

            return simpleUsername;
        }

        return (String) cachedUsername;
    }

    // 사용자 조회
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}
