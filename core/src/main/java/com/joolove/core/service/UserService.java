package com.joolove.core.service;

import com.joolove.core.domain.member.User;
import com.joolove.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    //회원 가입
    @Transactional
    public UUID join(User user) {
        userRepository.save(user);
        return user.getId();
    }

    //회원 탈퇴
    @Transactional
    public UUID leave(User user) {
        userRepository.delete(user);
        return user.getId();
    }

    //회원 정보 업데이트
    @Transactional
    public UUID update(User user) {
        userRepository.save(user);
        return user.getId();
    }

    //전체 회원 조회
    public List<User> findAll() {
        return userRepository.findAllFast();
    }

    public User findOne(UUID userId) {
        return userRepository.getReferenceById(userId);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsernameWithRelations(username);
    }
}
