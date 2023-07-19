package com.joolove.core.service;

import com.joolove.core.domain.member.User;
import com.joolove.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    // 사용자 가입
    @Transactional
    public User join(User user) {
        return userRepository.save(user);
    }

    // 사용자 탈퇴
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

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(UUID userId) {
        return userRepository.getReferenceById(userId);
    }

    // 사용자 정보 조회 (인증용)
    public User findByUsername(String username) {
        return userRepository.findByUsernameWithRelations(username);
    }
}
