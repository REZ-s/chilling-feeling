package com.joolove.core.service;

import com.joolove.core.domain.ERole;
import com.joolove.core.domain.ETargetCode;
import com.joolove.core.domain.auth.Password;
import com.joolove.core.domain.auth.Role;
import com.joolove.core.domain.auth.SocialLogin;
import com.joolove.core.domain.member.User;
import com.joolove.core.domain.member.UserRole;
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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
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
        userRoleRepository.findById(userRole.getId());
        System.out.println("---------------------------------------");
        userRepository.findByUsername(username);
        System.out.println("---------------------------------------");
        userDetailsService.loadUserByUsername(username);
        System.out.println("---------------------------------------");
        refreshTokenService.findByToken(RandomString.make(10));
    }

}
