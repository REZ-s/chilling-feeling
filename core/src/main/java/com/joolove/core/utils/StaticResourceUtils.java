package com.joolove.core.utils;

import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

public class StaticResourceUtils {
    private static final String[] staticResourcePatterns = {"/css/**", "/js/**", "/images/**", "/favicon.ico"};

    public static boolean isStaticResource(HttpServletRequest request) {
        return Arrays.stream(staticResourcePatterns)
                .anyMatch(pattern -> new AntPathMatcher().match(pattern, request.getServletPath()));
    }
}
