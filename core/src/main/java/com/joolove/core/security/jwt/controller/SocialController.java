package com.joolove.core.security.jwt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequiredArgsConstructor
@RequestMapping("/social/login")
public class SocialController {

    private final Environment env;

    private String baseURL = "http://localhost:8080/social/login/";

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;
    private String googleLoginURL = "https://accounts.google.com/o/oauth2/v2/auth";
    private String googleTokenURL = "https://oauth2.googleapis.com/token";
    private String googleProfileURL = "https://www.googleapis.com/oauth2/v3/userinfo";


    // 구글 로그인 페이지 테스트
    @GetMapping()
    public ModelAndView socialGoogleLogin(ModelAndView mav) {
        StringBuilder loginUrl = new StringBuilder()
                .append(env.getProperty(googleLoginURL))
                .append("?client_id=").append(googleClientId)
                .append("&response_type=code")
                .append("&scope=email%20profile")
                .append("&redirect_uri=").append(baseURL + "google");

        mav.addObject("loginUrl", loginUrl);
        mav.setViewName("login");
        return mav;
    }

    // 인증 완료 후 리다이렉트 페이지
    @GetMapping(value = "/{provider}")
    public ModelAndView redirectGoogle(ModelAndView mav, @RequestParam String code, @PathVariable String provider) {
        mav.addObject("code", code);
        mav.setViewName("redirect");
        return mav;
    }
}