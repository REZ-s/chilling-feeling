package com.joolove.core.security.jwt.controller;

import com.joolove.core.domain.auth.Password;
import com.joolove.core.domain.auth.Role;
import com.joolove.core.domain.member.User;
import com.joolove.core.domain.member.UserRole;
import com.joolove.core.domain.product.Goods;
import com.joolove.core.domain.product.GoodsDetails;
import com.joolove.core.domain.product.GoodsStats;
import com.joolove.core.dto.query.IGoodsView;
import com.joolove.core.dto.request.SignInRequest;
import com.joolove.core.dto.request.SignUpRequest;
import com.joolove.core.dto.request.UserRecommendationDailyRequest;
import com.joolove.core.repository.*;
import com.joolove.core.security.jwt.repository.PasswordRepository;
import com.joolove.core.service.GoodsService;
import com.joolove.core.service.EmailServiceImpl;
import com.joolove.core.service.SMSServiceImpl;
import com.joolove.core.service.UserService;
import com.joolove.core.utils.algorithm.RecommendationComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@CrossOrigin(originPatterns = "*", allowCredentials = "true", maxAge = 3600)
@Controller
@RequiredArgsConstructor
public class LoginController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final SocialLoginRepository socialLoginRepository;
    private final GoodsService goodsService;
    private final EmailServiceImpl emailService;
    private final SMSServiceImpl smsService;
    private final PasswordRepository passwordRepository;
    private final GoodsDetailsRepository goodsDetailsRepository;
    private final GoodsStatsRepository goodsStatsRepository;
    private final RecommendationComponent recommendationComponent;



    @GetMapping("/cf_login")
    public String cfLogin(Model model) {
        model.addAttribute("request", SignInRequest.buildEmpty());
        return "cf_login_page";
    }

    @PostMapping("/cf_login2")
    public String cfLogin2(Model model,
                           @Valid @ModelAttribute("request") SignInRequest request) {
        model.addAttribute("request", request);
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
        User user = userService.findByUsername(email);
        if (user == null) {
            return ResponseEntity.ok().body("invalid");
        }

        if (socialLoginRepository.existsByUser(user)) {
            return ResponseEntity.ok().body("valid-correct");
        }

        return ResponseEntity.ok().body("valid-incorrect");
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
        model.addAttribute("request", SignUpRequest.buildEmpty());
        return "cf_join_page";
    }

    @PostMapping(value = "/cf_join2")
    public String cfJoin2(@ModelAttribute("request") SignUpRequest request) {
        return "cf_join_page2";
    }

    @PostMapping(value = "/cf_join3")
    public String cfJoin3(@ModelAttribute("request") SignUpRequest request) {
        return "cf_join_page3";
    }

    @PostMapping(value = "/cf_join/complete")
    public String cfJoin4(@Valid @ModelAttribute("request") SignUpRequest request) {
        // 여기가 회원가입 최종 관문이니 넘어온 데이터를 @Valid 사용해서 검증

        // 사용자 접근 권한 생성
        List<Role> roles = new ArrayList<>();
        Role role = roleRepository.findByName(Role.ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(role);

        // 사용자 계정 생성
        User user = User.builder()
                .username(request.getUsername())
                .accountType((short) 1)
                .build();

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

    // 상품 검색 처음
    @GetMapping("/cf_search")
    public String searchPage(Model model) {
        return "cf_search_page";
    }

    // 상품 검색 결과
    @GetMapping("/cf_search/result")
    public String searchResult(Model model,
                               @RequestParam("query") String query) {
        if (!StringUtils.hasText(query)) {
            return "redirect:/cf_search_page";
        }

        model.addAttribute("goodsViewList", goodsService.findGoodsList(query, null, null, null, null));
        model.addAttribute("query", query);
        return "cf_search_result_page";
    }

    // 처음 카테고리 페이지가 로딩될 때
    @GetMapping("/cf_category")
    public String categoryPage(Model model) {
        model.addAttribute("goodsViewList", goodsService.findGoodsList(null, "전체", null, null, null));
        return "cf_category_page";
    }

    // 상품 1개에 대한 상세
    @GetMapping(("/cf_goods/{name}"))
    public String goodsPage(Model model,
                            @PathVariable("name") String name) {
        model.addAttribute("goodsViewDetails", goodsService.findGoodsDetail(name));
        return "cf_goods_page";
    }

    // 실시간 API 호출 (예: 검색하거나 카테고리를 선택했을 때)
    @GetMapping("/goods")
    @ResponseBody
    public ResponseEntity<List<IGoodsView>> getGoodsList(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "sort", required = false) String sort) {
        return ResponseEntity.ok().body(goodsService.findGoodsList(name, type, page, size, sort));
    }

    @GetMapping("/test/goods")
    @ResponseBody
    public ResponseEntity<?> addGoodsTest() {

        Goods goods = Goods.builder()
                .name("짐빔 화이트")
                .salesStatus((short)1)
                .build();

        GoodsDetails goodsDetails = GoodsDetails.alcoholBuilder()
                .goods(goods)
                .name("짐빔 화이트")
                .engName("Jim Beam White")
                .color("white")
                .colorImageUrl("https://cdn.veluga.kr/icons/tasting-note/color/brick-red.svg")
                .description("테스트 위스키")
                .descriptionImageUrl("/images/item-rep01.png")
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
                .goods(goods)
                .label("가성비")
                .score("4.4")
                .reviewCount(100)
                .build();

        goodsService.addGoods(goods);
        goodsDetailsRepository.save(goodsDetails);
        goodsStatsRepository.save(goodsStats);

        return ResponseEntity.ok().body("valid");
    }

    @GetMapping("/cf_recommendation_base")
    public String recommendBasePage() {
        return "cf_recommendation_base_page";
    }

    @GetMapping("/cf_recommendation_base2")
    public String recommendBasePage2(Model model,
                                     @RequestParam("abvLimit") String abvLimit) {
        model.addAttribute("abvLimit", abvLimit);
        return "cf_recommendation_base_page2";
    }

    @GetMapping("/cf_recommendation_daily")
    public String recommendDailyPage() {
        return "cf_recommendation_daily_page";
    }

    @GetMapping("/cf_recommendation_daily2")
    public String recommendDailyPage2(@AuthenticationPrincipal String username,
                                      @RequestParam String feeling) {
        recommendationComponent.setUserRecommendationDaily(
                UserRecommendationDailyRequest.builder()
                        .username(username)
                        .recentFeeling(feeling)
                        .build()
        );

        return "cf_recommendation_daily_page2";
    }

    @GetMapping("/cf_cart")
    public String cartPage(Model model) {
        // 사용자 이름으로 장바구니에 있는 상품만 조회해야한다.


        model.addAttribute("goodsViewList", goodsService.findGoodsList(null, "전체", null, null, null));
        return "cf_cart_page";
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