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
        //validateDuplicateUser(user);
        userRepository.save(user);
        return user.getId();
    }

    //회원 탈퇴
    @Transactional
    public UUID leave(User user) {
        //validateNotExistUser(user);
        userRepository.delete(user);
        return user.getId();
    }

    //회원 정보 업데이트
    @Transactional
    public UUID update(User user) {
        //validateNotExistUser(user);
        userRepository.save(user);
        return user.getId();
    }

    //전체 회원 조회
    public List<User> findAll() {
/*        List<User> users = new ArrayList<>();

        for (Object[] ob : userRepository.findAllFastV0()) {
            List<Object> objects = Arrays.stream(ob).toList();
            User user = User.builder().
                    id((UUID) objects.get(0)).
                    userName((String) objects.get(1)).
                    accountType((short) objects.get(2)).
                    build();
            users.add(user);
        }

        return users;*/

        return userRepository.findAllFast();
    }

    public User findOne(UUID userId) {
        return userRepository.getReferenceById(userId);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}
