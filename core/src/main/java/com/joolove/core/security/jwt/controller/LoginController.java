package com.joolove.core.security.jwt.controller;

import com.joolove.core.domain.ERole;
import com.joolove.core.domain.auth.Password;
import com.joolove.core.domain.auth.Role;
import com.joolove.core.domain.auth.SocialLogin;
import com.joolove.core.domain.member.User;
import com.joolove.core.domain.member.UserRole;
import com.joolove.core.domain.product.Goods;
import com.joolove.core.repository.GoodsRepository;
import com.joolove.core.repository.RoleRepository;
import com.joolove.core.repository.UserRepository;
import com.joolove.core.security.jwt.utils.JwtUtils;
import com.joolove.core.security.service.GoodsService;
import com.joolove.core.security.service.LogoutTokenService;
import com.joolove.core.security.service.RefreshTokenService;
import com.joolove.core.security.service.UserPrincipal;
import com.joolove.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(originPatterns = "*", allowCredentials = "true", maxAge = 3600)
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final GoodsService goodsService;

    @GetMapping("/")
    public String getMainPage(Model model) {
        model.addAttribute("allUserList", userService.findAll());
        return "all_user_list";
    }

    @GetMapping("/sign_up")
    public String joinForm(Model model) {
        model.addAttribute("request", User.SignupRequest.buildEmpty());
        return "sign_up";
    }

    @PostMapping("/sign_up/create")
    public String joinByForm(Model model, @Valid @ModelAttribute User.SignupRequest request) {
        List<Role> roles = new ArrayList<>();
        Role role = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(role);

        //Create new user's account
        User user = User.builder()
                .username(request.getUsername())
                .accountType((short)1)
                .build();

        com.joolove.core.domain.auth.Authentication authentication = com.joolove.core.domain.auth.Authentication.builder()
                .user(user)
                .email(request.getEmail())
                .sex("남자")
                .phoneNumber("010-7369-6639")
                .birthday(LocalDate.of(1992, 12, 10))
                .country("Korea")
                .gatherAgree(true)
                .build();
        user.setAuthentication(authentication);

        Password password = Password.builder()
                .user(user)
                .pw(passwordEncoder.encode(request.getPassword()))
                .build();
        user.setPassword(password);

        List<UserRole> userRoles = new ArrayList<>();
        for (Role r : roles) {
            UserRole userRole = UserRole.builder()
                    .user(user)
                    .role(r)
                    .build();
            userRoles.add(userRole);
        }
        user.setRoles(userRoles);

        userRepository.save(user);

        return "redirect:/sign_in";
    }

    @GetMapping("/sign_in")
    public String loginForm(Model model) {
        model.addAttribute("request", User.SigninRequest.buildEmpty());
        return "sign_in";
    }

    @GetMapping("/cf_login")
    public String cfLogin(Model model) {
        model.addAttribute("request", User.SigninRequest.buildEmpty());
        return "cf_login_page";
    }

    @GetMapping("/my_page")
    public String myPage(Model model, Authentication authentication) {
        String username = authentication.getPrincipal().toString();
        User user = userService.findByUsername(username);
        model.addAttribute("user", user);
        return "my_page";
    }

    @GetMapping("/main")
    public String mainPage() {
        return "main";
    }

    @GetMapping("/search")
    public String searchPage(Model model) {
        model.addAttribute("goods", Goods.SearchRequest.buildEmpty());
        return "search_main";
    }

    @PostMapping("/search/goods")
    public String searchItemPage(Model model, @Valid @ModelAttribute Goods.SearchRequest request) {
        List<Goods> goodsList = goodsService.findListGoods(request.getName());
        model.addAttribute("goodsList", goodsList);
        return "search_goods_list";
    }

    @GetMapping("/form/loginInfo")
    @ResponseBody
    public String formLoginInfo(Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        return principal.getUser().toString();
    }

    @GetMapping("/oauth/loginInfo")
    @ResponseBody
    public String oauthLoginInfo(Authentication authentication) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        return oAuth2User.toString();
    }

    @GetMapping("/loginInfo")
    @ResponseBody
    public String loginInfo(Authentication authentication) {
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