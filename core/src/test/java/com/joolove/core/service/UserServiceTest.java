package com.joolove.core.service;

import com.joolove.core.domain.member.User;
import com.joolove.core.repository.UserRepository;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

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

    @Test
    @Sql({"classpath:h2/sample_data.sql"})
    public void 회원가입() throws Exception {
        User user = User.builder().
                username(RandomString.make()).
                accountType((short)1).
                build();

        UUID saveId = userService.join(user);
        System.out.println("[가입한] user_id : " + saveId);

        System.out.println("다음 단계에서 insert. why? findUsers 내부에 JPQL 나가면서 flush 자동호출");
        List<User> userList = userService.findAll();
        for (User u : userList) {
            System.out.println(u.toString());
        }

        UUID deletedId = userService.leave(userService.findOne(saveId));
        System.out.println("[탈퇴한] user_id : " + deletedId);
        assertThat("must be empty", userRepository.findById(deletedId).isEmpty());

        List<User> userList2 = userService.findAll();
        for (User u : userList2) {
            System.out.println(u.toString());
        }

    }

}
