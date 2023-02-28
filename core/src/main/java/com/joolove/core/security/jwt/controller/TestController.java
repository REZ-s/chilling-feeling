package com.joolove.core.security.jwt.controller;

import com.joolove.core.domain.auth.SocialLogin;
import com.joolove.core.domain.member.User;
import com.joolove.core.repository.UserRepository;
import com.joolove.core.security.service.UserPrincipal;
import com.joolove.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class TestController {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserService userService;

    @GetMapping("/")
    public String getMainPage(Model model) {
        model.addAttribute("allUserList", userService.findAll());
        return "test_main";
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "join";
    }

    @PostMapping("/join")
    public String join(@ModelAttribute User user) {
//        user.setRole(Role.ROLE_USER);
//
//        String encodePwd = bCryptPasswordEncoder.encode(user.getPassword());
//        user.setPassword(encodePwd);
//
//        userRepository.save(user);  //반드시 패스워드 암호화해야함

        //userService 에서 join 메소드를 작성하자
        return "redirect:/loginForm";
    }

    @GetMapping("/form/loginInfo")
    @ResponseBody
    public String formLoginInfo(Authentication authentication, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        return principal.getUser().toString();
    }

    @GetMapping("/oauth/loginInfo")
    @ResponseBody
    public String oauthLoginInfo(Authentication authentication, @AuthenticationPrincipal OAuth2User oAuth2UserPrincipal) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2UserPrincipal.getAttributes();
        //Map<String, Object> attributes2 = oAuth2User.getAttributes();
        // 출력결과 : attr == attr2
        return attributes.toString();
    }


    @GetMapping("/loginInfo")
    @ResponseBody
    public String loginInfo(Authentication authentication, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        String result = "";
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        if (SocialLogin.convertToProvider(principal.getUser().getSocialLogin().getProviderCode()) == null) {
            result = result + "Form 로그인 : " + principal;
        }
        else {
            result = result + "OAuth2 로그인 : " + principal;
        }

        return result;
    }

    // Access auth test
    @GetMapping("/all")
    @ResponseBody
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/user")
    @ResponseBody
    public String userAccess() {
        return "User Content.";
    }

    @GetMapping("/manager")
    @ResponseBody
    public String managerAccess() {
        return "Manager Board.";
    }

    @GetMapping("/admin")
    @ResponseBody
    public String adminAccess() {
        return "Admin Board.";
    }

}