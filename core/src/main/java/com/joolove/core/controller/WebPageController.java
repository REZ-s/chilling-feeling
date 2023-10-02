package com.joolove.core.controller;

import com.joolove.core.dto.query.IGoodsView;
import com.joolove.core.dto.request.SignInRequest;
import com.joolove.core.dto.request.SignUpRequest;
import com.joolove.core.dto.request.UserRecommendationDailyRequest;
import com.joolove.core.service.GoodsService;
import com.joolove.core.service.UserService;
import com.joolove.core.utils.RecommendationUtils;
import com.joolove.core.utils.aop.LoginState;
import com.joolove.core.utils.aop.LogoutState;
import com.joolove.core.utils.aop.SearchLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Controller
@RequiredArgsConstructor
@CrossOrigin(originPatterns = "*", allowCredentials = "true", maxAge = 3600)
public class WebPageController {
    private final UserService userService;
    private final GoodsService goodsService;
    private final RecommendationUtils recommendationUtils;

    // 메인 페이지
    @GetMapping("/")
    public String main(Model model) throws ExecutionException, InterruptedException {
        String username = userService.getUsernameByAuthentication();
        if (Objects.equals(username, "Guest")) {
            model.addAttribute("goodsViewList", goodsService.findGoodsList(null, "전체", 0, 10, null));
        } else {
            List<IGoodsView> userRecommendationGoodsList = recommendationUtils.getUserRecommendationGoodsList(username);
            if (userRecommendationGoodsList == null || userRecommendationGoodsList.isEmpty()) {
                userRecommendationGoodsList = goodsService.findGoodsList(null, "전체", 0, 10, null);
            }

            model.addAttribute("goodsViewList", userRecommendationGoodsList);
        }

        model.addAttribute("username", userService.getUsernameByAuthentication());
        model.addAttribute("goodsViewDetails", goodsService.findGoodsDetailsRandom().get());
        return "cf_main_page";
    }

    // 로그인 페이지
    @LogoutState
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("request", SignInRequest.buildEmpty());
        return "cf_login_page";
    }

    @LogoutState
    @PostMapping("/login/password")
    public String loginToPassword(Model model, @ModelAttribute("request") SignInRequest request) {
        model.addAttribute("request", request);
        return "cf_login_page2";
    }

    @LogoutState
    @GetMapping("/join")
    public String join(Model model) {
        model.addAttribute("request", SignUpRequest.buildEmpty());
        return "cf_join_page";
    }

    @LogoutState
    @PostMapping("/join/password")
    public String joinToPassword(@ModelAttribute("request") SignUpRequest request) {
        return "cf_join_page2";
    }

    @LogoutState
    @PostMapping("/join/phone")
    public String joinToPhone(@ModelAttribute("request") SignUpRequest request) {
        return "cf_join_page3";
    }

    @LogoutState
    @PostMapping("/join/submit")
    public String joinToSubmit(@Valid @ModelAttribute("request") SignUpRequest request) {
        userService.joinByForm(request);
        return "redirect:/";
    }

    // 상품 검색 첫 페이지
    @GetMapping("/search")
    public String search() {
        return "cf_search_page";
    }

    // 상품 검색 결과
    @SearchLog
    @GetMapping("/search/result")
    public String searchResult(Model model, @RequestParam("query") String query, @RequestParam("deviceId") UUID deviceId) throws ExecutionException, InterruptedException {
        if (!StringUtils.hasText(query)) {
            return "redirect:/search";
        }

        model.addAttribute("goodsViewList", goodsService.findGoodsList(query, "전체", 0, 10, null));
        model.addAttribute("goodsViewListCount", goodsService.getGoodsListCount(query, "전체"));
        model.addAttribute("query", query);
        return "cf_search_result_page";
    }

    // 카테고리 첫 페이지
    @GetMapping("/category")
    public String category(Model model) throws ExecutionException, InterruptedException {
        model.addAttribute("goodsViewList", goodsService.findGoodsList(null, "전체", 0, 10, null));
        model.addAttribute("goodsViewListCount", goodsService.getGoodsListCount(null, "전체"));
        return "cf_category_page";
    }

    // 상품 1개에 대한 상세 페이지
    @GetMapping(("/goods/{name}"))
    public String goodsName(Model model, @PathVariable("name") String name) {
        model.addAttribute("goodsViewDetails", goodsService.findGoodsDetails(name));
        return "cf_goods_page";
    }

    // 기본 취향 설정 페이지
    @LoginState
    @GetMapping("/recommendation/base")
    public String recommendationBase(Model model) {
        model.addAttribute("username", userService.getUsernameByAuthentication());
        return "cf_recommendation_base_page";
    }

    @LoginState
    @GetMapping("/recommendation/base/submit")
    public String recommendationBase2(Model model, @RequestParam("username") String username, @RequestParam("abvLimit") String abvLimit) {
        model.addAttribute("username", username);
        model.addAttribute("abvLimit", abvLimit);
        return "cf_recommendation_base_page2";
    }

    // 오늘의 추천 페이지
    @LoginState
    @GetMapping("/recommendation/daily")
    public String recommendationDaily(Model model) {
        model.addAttribute("username", userService.getUsernameByAuthentication());
        return "cf_recommendation_daily_page";
    }

    @LoginState
    @GetMapping("/recommendation/daily/submit")
    public String recommendationDaily2(@RequestParam("username") String username, @RequestParam("feeling") String feeling) {
        boolean isPassed = recommendationUtils.setUserRecommendationDaily(
                UserRecommendationDailyRequest.builder()
                        .username(username)
                        .recentFeeling(feeling)
                        .build()
        );

        return isPassed ? "cf_recommendation_daily_page2" : "redirect:/login";
    }

    // 장바구니 페이지
    @LoginState
    @GetMapping("/cart")
    public String cart() {
        return "cf_cart_page";
    }

    // 위시리스트 페이지
    @LoginState
    @GetMapping("/wish")
    public String wish() {
        return "cf_wishlist_page";
    }

    // 마이페이지
    @LoginState
    @GetMapping("/me")
    public String me(Model model) {
        model.addAttribute("username", userService.getUsernameByAuthentication());
        return "cf_my_page";
    }

    // 준비중 페이지
    @GetMapping("/ready")
    public String ready() {
        return "cf_ready_page";
    }

}