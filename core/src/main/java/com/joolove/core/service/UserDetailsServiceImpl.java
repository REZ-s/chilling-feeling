package com.joolove.core.service;

import com.joolove.core.utils.oauth2.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Map<String, String> userInfo = userService.getUsernamePasswordByUsernameForSecurityFilter(username);
        if (userInfo == null) {
            logger.info("User not found with username: " + username);
            return null;
        }

        return UserPrincipal.buildUserDetails(userInfo.get("username"), userInfo.get("password"));
    }

}