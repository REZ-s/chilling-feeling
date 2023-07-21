package com.joolove.core.controller;

import com.joolove.core.dto.query.IGoodsView;
import com.joolove.core.dto.request.SignInRequest;
import com.joolove.core.dto.request.SignUpRequest;
import com.joolove.core.dto.request.UserRecommendationDailyRequest;
import com.joolove.core.service.GoodsService;
import com.joolove.core.service.UserService;
import com.joolove.core.utils.RecommendationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@CrossOrigin(originPatterns = "*", allowCredentials = "true", maxAge = 3600)
public class WebController {
    private final UserService userService;
    private final GoodsService goodsService;
    private final RecommendationUtils recommendationUtils;

    @GetMapping("/")
    public String main() {
        return "cf_main_page";
    }

    @GetMapping("/login")
    public String login(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return "redirect:/";
        }

        model.addAttribute("request", SignInRequest.buildEmpty());
        return "cf_login_page";
    }

    @PostMapping("/login/submit")
    public String loginToSubmit(Model model, @Valid @ModelAttribute("request") SignInRequest request) {
        model.addAttribute("request", request);
        return "cf_login_page2";
    }

    @GetMapping("/join")
    public String join(Model model) {
        model.addAttribute("request", SignUpRequest.buildEmpty());
        return "cf_join_page";
    }

    @PostMapping("/join/password")
    public String joinToPassword(@ModelAttribute("request") SignUpRequest request) {
        return "cf_join_page2";
    }

    @PostMapping("/join/phone")
    public String joinToPhone(@ModelAttribute("request") SignUpRequest request) {
        return "cf_join_page3";
    }

    @PostMapping("/join/submit")
    public String joinToSubmit(@Valid @ModelAttribute("request") SignUpRequest request) {
        userService.join(request);
        return "redirect:/";
    }

    // 상품 검색 첫 페이지
    @GetMapping("/search")
    public String search() {
        return "cf_search_page";
    }

    // 상품 검색 결과
    @GetMapping("/search/result")
    public String searchResult(Model model, @RequestParam("query") String query) {
        if (!StringUtils.hasText(query)) {
            return "redirect:/search";
        }

        model.addAttribute("goodsViewList", goodsService.findGoodsList(query, null, null, null, null));
        model.addAttribute("query", query);
        return "cf_search_result_page";
    }

    // 카테고리 첫 페이지
    @GetMapping("/category")
    public String categoryPage(Model model) {
        model.addAttribute("goodsViewList", goodsService.findGoodsList(null, "전체", null, null, null));
        return "cf_category_page";
    }

    // 상품 1개에 대한 상세
    @GetMapping(("/goods/{name}"))
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

    @GetMapping("/recommendation/base")
    public String recommendBasePage() {
        return "cf_recommendation_base_page";
    }

    @GetMapping("/recommendation/base2")
    public String recommendBasePage2(Model model,
                                     @RequestParam("abvLimit") String abvLimit) {
        model.addAttribute("abvLimit", abvLimit);
        return "cf_recommendation_base_page2";
    }

    @GetMapping("/recommendation/daily")
    public String recommendDailyPage() {
        return "cf_recommendation_daily_page";
    }

    @GetMapping("/recommendation/daily2")
    public String recommendDailyPage2(@AuthenticationPrincipal String username,
                                      @RequestParam String feeling) {
        recommendationUtils.setUserRecommendationDaily(
                UserRecommendationDailyRequest.builder()
                        .username(username)
                        .recentFeeling(feeling)
                        .build()
        );
        return "cf_recommendation_daily_page2";
    }

    @GetMapping("/cart")
    public String cartPage(Model model) {
        // 사용자 이름으로 장바구니에 있는 상품만 조회해야한다.

        model.addAttribute("goodsViewList", goodsService.findGoodsList(null, "전체", null, null, null));
        return "cf_cart_page";
    }

}