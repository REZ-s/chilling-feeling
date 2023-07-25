package com.joolove.core.utils;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 *  Hashing method (password + salt)
 */
@Component
@RequiredArgsConstructor
public class PasswordUtils extends BCryptPasswordEncoder {

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (encodedPassword == null || encodedPassword.isEmpty()) {
            return super.matches(rawPassword, encode(rawPassword));
        }

        return super.matches(rawPassword, encodedPassword);
    }
}
