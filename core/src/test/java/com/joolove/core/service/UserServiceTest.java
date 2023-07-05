package com.joolove.core.service;

import com.joolove.core.repository.RoleRepository;
import com.joolove.core.repository.UserRepository;
import com.joolove.core.repository.UserRoleRepository;
import com.joolove.core.security.service.RefreshTokenService;
import com.joolove.core.security.service.UserDetailsServiceImpl;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RefreshTokenService refreshTokenService;
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    UserRoleRepository userRoleRepository;

    @Autowired
    RoleRepository roleRepository;

    @Test
    public void EnumTest() {
        String username = "test1@naver.com";
/*
        User user = User.builder()
                .username(username)
                .accountType((short) 1)
                .build();

        Password password = Password.builder()
                .user(user)
                .pw(UUID.randomUUID().toString())
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

        userRepository.save(user);
        System.out.println("---------------------------------------");
        userRoleRepository.findById(userRole.getId());*/
//        System.out.println("---------------------------------------");
        userRepository.findByUsername(username);
        System.out.println("---------------------------------------");
        userRepository.findByUsernameWithRelations(username);
        System.out.println("---------------------------------------");
        refreshTokenService.findByToken(RandomString.make(10));
    }

}
