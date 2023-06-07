package com.joolove.core.security.jwt.controller;

import com.joolove.core.domain.ERole;
import com.joolove.core.domain.auth.Password;
import com.joolove.core.domain.auth.Role;
import com.joolove.core.domain.auth.SocialLogin;
import com.joolove.core.domain.member.User;
import com.joolove.core.domain.member.UserRole;
import com.joolove.core.domain.product.Goods;
import com.joolove.core.domain.product.GoodsDetails;
import com.joolove.core.domain.product.GoodsStats;
import com.joolove.core.repository.*;
import com.joolove.core.security.jwt.repository.AuthenticationRepository;
import com.joolove.core.security.jwt.repository.PasswordRepository;
import com.joolove.core.security.jwt.utils.JwtUtils;
import com.joolove.core.security.service.GoodsService;
import com.joolove.core.security.service.LogoutTokenService;
import com.joolove.core.security.service.RefreshTokenService;
import com.joolove.core.security.service.UserPrincipal;
import com.joolove.core.service.EmailServiceImpl;
import com.joolove.core.service.SMSServiceImpl;
import com.joolove.core.service.UserService;
import lombok.Builder;
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
import org.springframework.util.StringUtils;
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
import java.util.Optional;
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
    private final GoodsDetailsRepository goodsDetailsRepository;
    private final GoodsStatsRepository goodsStatsRepository;
    private final EmailServiceImpl emailService;
    private final SMSServiceImpl smsService;
    private final PasswordRepository passwordRepository;
    private final GoodsRepository goodsRepository;

    @GetMapping("/cf_login")
    public String cfLogin(Model model) {
        model.addAttribute("request", User.SigninRequest.buildEmpty());
        return "cf_login_page";
    }

    @PostMapping("/cf_login2")
    public String cfLogin2(Model model, @Valid @ModelAttribute("request") User.SigninRequest request) {
        return "cf_login_page2";
    }

    @PostMapping("/get_authentication_code/email")
    @ResponseBody
    public ResponseEntity<?> getAuthenticationCodeEmail(@Valid @RequestBody String email)
            throws Exception {
        String code = emailService.sendAuthCode(email);
        return ResponseEntity.ok().body(code);
    }

    @PostMapping("/get_authentication_code/sms")
    @ResponseBody
    public ResponseEntity<?> getAuthenticationCodeSMS(@Valid @RequestBody String phoneNumber)
            throws Exception {
        String code = smsService.sendAuthCode(phoneNumber);
        return ResponseEntity.ok().body(code);
    }

    @PostMapping("/check_authentication_code/email")
    @ResponseBody
    public ResponseEntity<?> checkAuthenticationCodeEmail(@Valid @RequestBody String code) {
        if (Objects.equals(emailService.getAuthCode(), code)) {
            return ResponseEntity.ok().body("valid");
        }
        return ResponseEntity.ok().body("invalid");
    }

    @PostMapping("/check_authentication_code/sms")
    @ResponseBody
    public ResponseEntity<?> checkAuthenticationCodeSMS(@Valid @RequestBody String code) {
        if (Objects.equals(smsService.getAuthCode(), code)) {
            return ResponseEntity.ok().body("valid");
        }
        return ResponseEntity.ok().body("invalid");
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

    @PostMapping("/check_password")
    @ResponseBody
    public ResponseEntity<?> checkPassword(@Valid @RequestBody String password) {
        if (passwordRepository.existsByPw(passwordEncoder.encode(password))) {
            return ResponseEntity.ok().body("valid");
        }

        return ResponseEntity.ok().body("invalid");
    }

    @GetMapping("/cf_join")
    public String cfJoin(Model model) {
        model.addAttribute("request", User.SignupRequest.buildEmpty());
        return "cf_join_page";
    }

    @PostMapping(value = "/cf_join2")
    public String cfJoin2(Model model, @ModelAttribute("request") User.SignupRequest request) {
        return "cf_join_page2";
    }

    @PostMapping(value = "/cf_join3")
    public String cfJoin3(Model model, @ModelAttribute("request") User.SignupRequest request) {
        return "cf_join_page3";
    }

    @PostMapping(value = "/cf_join/complete")
    public String cfJoin4(Model model, @Valid @ModelAttribute("request") User.SignupRequest request) {
        // 여기가 회원가입 최종 관문이니 넘어온 데이터를 @Valid 사용해서 검증

        // 사용자 접근 권한 생성
        List<Role> roles = new ArrayList<>();
        Role role = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(role);

        // 사용자 계정 생성
        User user = User.builder()
                .username(request.getUsername())
                .accountType((short) 1)
                .build();

        com.joolove.core.domain.auth.Authentication authentication = com.joolove.core.domain.auth.Authentication.builder()
                .user(user)
                .email(request.getUsername())
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

        return "redirect:/cf_main_page";
    }

    // 지금 고민되는게, 이 두가지가 있음
    // 로그인한 상태인지를 검사하고, 로그인한 상태로 메인화면에 진입한다.
    // 비로그인 상태로 메인화면에 진입한다.
    @GetMapping("/cf_main")
    public String mainPage() {
        return "cf_main_page";
    }

    @GetMapping("/cf_search")
    public String searchPage(Model model) {
        return "cf_search_page";
    }

    @GetMapping("/cf_search/result")
    public String searchResult(Model model, @RequestParam("query") String query) {
        if (!StringUtils.hasText(query)) {
            return "redirect:/cf_search_page";
        }

        model.addAttribute("goodsViewList",
                goodsService.findGoodsListByPaging(query, null, null, null, null));
        model.addAttribute("query", query);
        return "cf_search_result_page";
    }

    // 처음 카테고리 페이지가 로딩될 때
    @GetMapping("/cf_category")
    public String categoryPage(Model model) {
        model.addAttribute("goodsViewList",
                goodsService.findGoodsListByPaging(null, "전체", null, null, null));
        return "cf_category_page";
    }

    // 검색하거나 카테고리를 선택했을 때
    @GetMapping("/goods")
    @ResponseBody
    public ResponseEntity<List<Goods.GoodsView>> getGoodsList(
            @RequestParam("name") String name,
            @RequestParam("category") String category,
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size,
            @RequestParam("sort") String sort) {
        return ResponseEntity.ok().body(goodsService.findGoodsListByPaging(name, category, page, size, sort));
    }

    @GetMapping("/test/goods")
    @ResponseBody
    public ResponseEntity<?> addGoodsTest() {
        GoodsDetails goodsDetails = GoodsDetails.alcoholBuilder()
                .name("짐빔 화이트")
                .engName("Jim Beam White")
                .color("white")
                .description("테스트 위스키")
                .summary("테스트 위스키")
                .country("korea")
                .company("테스트 컴퍼니")
                .supplier("테스트 컴퍼니")
                .degree("10.0")
                .imageUrl("/images/item-rep01.png")
                .type("위스키")
                .opt1Value("opt1Value")
                .opt2Value("opt2Value")
                .opt3Value("opt3Value")
                .opt4Value("opt4Value")
                .opt5Value("opt5Value")
                .opt6Value("opt6Value")
                .opt7Value("opt7Value")
                .build();

        GoodsStats goodsStats = GoodsStats.builder()
                .label("가성비")
                .score("4.4")
                .reviewCount(100)
                .build();

        Goods goods = Goods.builder()
                .name("짐빔 화이트")
                .salesStatus((short)1)
                .goodsDetails(goodsDetails)
                .goodsStats(goodsStats)
                .build();

        goodsService.addGoods(goods);

        return ResponseEntity.ok().body("valid");
    }


    // 사용자 계정 테스트
    @GetMapping("/")
    public String getMainPage(Model model) {
        model.addAttribute("allUserList", userService.findAll());
        return "all_user_list";
    }

    // 접근 권한 테스트
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