package com.joolove.core.service;

import com.joolove.core.domain.member.User;
import com.joolove.core.repository.UserRepository;
import com.joolove.core.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RedisUtils redisUtils;

    // 사용자 회원 가입
    @Transactional
    public User join(User user) {
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
        return userRepository.save(user);
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
