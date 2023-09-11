//package com.joolove.core;
//
//import com.joolove.core.domain.auth.RefreshToken;
//import com.joolove.core.domain.member.User;
//import com.joolove.core.repository.RefreshTokenRepository;
//import com.joolove.core.repository.UserRepository;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//import static org.springframework.test.util.AssertionErrors.assertEquals;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest
//@Transactional
//public class RedisTest {
//    @Autowired
//    private RefreshTokenRepository refreshTokenRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    private String username = "tester123@naver.com";
//
//    @AfterEach
//    public void tearDown() {
//        refreshTokenRepository.deleteAll();
//        userRepository.delete(userRepository.findByUsername(username));
//    }
//
//    @Test
//    public void JWT_RefreshToken_Test() {
//        //given
//        UUID id = UUID.randomUUID();
//        LocalDateTime refreshTime = LocalDateTime.now().plusSeconds(3600);
//        String token = UUID.randomUUID().toString();
//
//        User user = User.builder()
//                .accountType((short) 1)
//                .username(username)
//                .build();
//
//        RefreshToken refreshToken = RefreshToken.builder()
//                .id(id)
//                .username(user.getUsername())
//                .expiryDate(refreshTime)
//                .token(token)
//                .build();
//
//        // when
//        userRepository.save(user);
//        RefreshToken token2 = refreshTokenRepository.save(refreshToken);
//
//        //then
//        RefreshToken JPARefreshToken = refreshTokenRepository.getReferenceById(id);
//        assertEquals("jpa : ", token2.getToken(), token);
//    }
//}
