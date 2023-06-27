package com.joolove.core.service;

import com.joolove.core.domain.ETargetCode;
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
    public void EnumTest() {
        ETargetCode targetCode = ETargetCode.valueOf("GOODS");
        ETargetCode targetCode2 = ETargetCode.GOODS;
        System.out.println(targetCode);
        System.out.println(targetCode2.name());
    }

}
