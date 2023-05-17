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
import com.joolove.core.security.jwt.repository.AuthenticationRepository;
import com.joolove.core.security.jwt.repository.PasswordRepository;
import com.joolove.core.security.jwt.utils.JwtUtils;
import com.joolove.core.security.service.GoodsService;
import com.joolove.core.security.service.LogoutTokenService;
import com.joolove.core.security.service.RefreshTokenService;
import com.joolove.core.security.service.UserPrincipal;
import com.joolove.core.service.EmailServiceImpl;
import com.joolove.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@CrossOrigin(originPatterns = "*", allowCredentials = "true", maxAge = 3600)
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationRepository authenticationRepository;
    private final PasswordEncoder passwordEncoder;
    private final GoodsService goodsService;
    private final EmailServiceImpl emailService;
    private final PasswordRepository passwordRepository;

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
                .accountType((short) 1)
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
        model.addAttribute("email", "");
        return "cf_login_page";
    }

    @PostMapping("/check_email")
    @ResponseBody
    public ResponseEntity<?> checkEmail(@Valid @RequestBody String email) {
        if (authenticationRepository.existsByEmail(email)) {
            if (userRepository.existsByUsername(email)) {
                return ResponseEntity.ok().body("valid-correct-access");
            }

            return ResponseEntity.ok().body("valid-incorrect-access");
        }

        return ResponseEntity.ok().body("invalid");
    }

    @PostMapping("/get_authentication_code/email")
    @ResponseBody
    public ResponseEntity<?> getAuthenticationCodeEmail(@Valid @RequestBody String email)
            throws Exception {
        String code = emailService.sendAuthCode(email);
        return ResponseEntity.ok().body(code);
    }

    @PostMapping("/get_authentication_code/phone")
    @ResponseBody
    public ResponseEntity<?> getAuthenticationCodePhone(@Valid @RequestBody String phoneNumber)
            throws Exception {
        String code = emailService.sendAuthCode(phoneNumber);
        return ResponseEntity.ok().body(code);
    }

    @PostMapping("/check_authentication_code/email")
    @ResponseBody
    public ResponseEntity<?> checkAuthenticationCodeEmail(@Valid @RequestBody String code) {
        if (Objects.equals(emailService.getEPw(), code)) {
            return ResponseEntity.ok().body("valid");
        }
        return ResponseEntity.ok().body("invalid");
    }

    @PostMapping("/check_authentication_code/phone")
    @ResponseBody
    public ResponseEntity<?> checkAuthenticationCodePhone(@Valid @RequestBody String code) {
        if (Objects.equals(emailService.getEPw(), code)) {
            return ResponseEntity.ok().body("valid");
        }
        return ResponseEntity.ok().body("invalid");
    }

    @PostMapping("/check_password")
    @ResponseBody
    public ResponseEntity<?> checkPassword(@Valid @RequestBody String password) {
        if (passwordRepository.existsByPw(passwordEncoder.encode(password))) {
            return ResponseEntity.ok().body("valid");
        }

        return ResponseEntity.ok().body("invalid");
    }

    @PostMapping(value = "/cf_login2", consumes = "application/x-www-form-urlencoded")
    public String cfLogin2(@Valid @RequestBody MultiValueMap<String, String> formData, Model model) {
        String encodedEmail = formData.getFirst("email");
        // email is not null
        String email = URLDecoder.decode(encodedEmail, StandardCharsets.UTF_8);

        User.SigninRequest request = User.SigninRequest.buildEmpty();
        request.setUsername(email);
        model.addAttribute("request", request);
        return "cf_login_page2";
    }

    @GetMapping("/cf_join")
    public String cfJoin(Model model) {
        model.addAttribute("request", User.SignupRequest.buildEmpty());
        return "cf_join_page";
    }

    @PostMapping(value = "/cf_join2", consumes = "application/x-www-form-urlencoded")
    public String cfJoin2(@Valid @RequestBody MultiValueMap<String, String> formData, Model model) {
        String encodedEmail = formData.getFirst("email");
        // email is not null
        String email = URLDecoder.decode(encodedEmail, StandardCharsets.UTF_8);

        User.SignupRequest request = User.SignupRequest.buildEmpty();
        request.setEmail(email);
        model.addAttribute("request", request);
        return "cf_join_page2";
    }

    @PostMapping(value = "/cf_join3", consumes = "application/x-www-form-urlencoded")
    public String cfJoin3(@Valid @RequestBody MultiValueMap<String, String> formData, Model model) {
        String encodedPassword = formData.getFirst("password");
        // password is not null
        String password = URLDecoder.decode(encodedPassword, StandardCharsets.UTF_8);

        Object requestObject = model.getAttribute("request");
        if (requestObject instanceof User.SignupRequest) {
            User.SignupRequest request =  (User.SignupRequest) requestObject;
            request.setPassword(password);
            model.addAttribute("request", request);
        } else {
            throw new RuntimeException("request is not User.SignupRequest");
        }

        return "cf_join_page3";
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
        } else {
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