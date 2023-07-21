package com.joolove.core.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 *  Hashing method (password + salt)
 */
@Component
@RequiredArgsConstructor
public class PasswordUtils extends BCryptPasswordEncoder {


}
