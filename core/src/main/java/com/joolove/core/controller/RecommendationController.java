package com.joolove.core.controller;

import com.joolove.core.dto.query.IGoodsView;
import com.joolove.core.dto.query.UserActivityElements;
import com.joolove.core.dto.request.UserRecommendationBaseRequest;
import com.joolove.core.dto.request.UserRecommendationDailyRequest;
import com.joolove.core.utils.RecommendationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.http.HttpHeaders;
import java.util.List;

@CrossOrigin(originPatterns = "*", allowCredentials = "true", maxAge = 3600)
@Controller
@RequiredArgsConstructor
public class RecommendationController {
    private final RecommendationUtils recommendationUtils;

    @PostMapping("/api/v1/recommendation/base")
    @ResponseBody
    public ResponseEntity<Object> setRecommendationBase(@Valid @RequestBody UserRecommendationBaseRequest userRecommendationBaseRequest) {
        String username = userRecommendationBaseRequest.getUsername();

        if (username == null || username.equals("anonymousUser")) {
            return ResponseEntity.ok().location(URI.create("redirect:/login")).build();
        }

        if (recommendationUtils.setUserRecommendationBase(userRecommendationBaseRequest)) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.internalServerError().build();
    }

    @PostMapping("/api/v1/recommendation/daily")
    @ResponseBody
    public ResponseEntity<Object> setRecommendationDaily(@Valid @RequestBody UserRecommendationDailyRequest userRecommendationDailyRequest) {
        String username = userRecommendationDailyRequest.getUsername();

        if (username == null || username.equals("anonymousUser")) {
            return ResponseEntity.ok().location(URI.create("redirect:/login")).build();
        }

        if (recommendationUtils.setUserRecommendationDaily(userRecommendationDailyRequest)) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.internalServerError().build();
    }

    @GetMapping("/api/v1/recommendation/daily")
    @ResponseBody
    public ResponseEntity<Object> getRecommendationDaily(@AuthenticationPrincipal String username) {
        if (username == null || username.equals("anonymousUser")) {
            return ResponseEntity.ok().location(URI.create("redirect:/login")).build();
        }

        List<IGoodsView> goodsViews = recommendationUtils.getUserRecommendationGoodsList(username);
        return ResponseEntity.ok().body(goodsViews);
    }

    @GetMapping("/api/v1/recommendation")
    @ResponseBody
    public ResponseEntity<Object> getRecommendation(@AuthenticationPrincipal String username) {
        if (username == null || username.equals("anonymousUser")) {
            return ResponseEntity.ok().location(URI.create("redirect:/login")).build();
        }

        List<IGoodsView> goodsViews = recommendationUtils.getUserRecommendationGoodsList(username);
        return ResponseEntity.ok().body(goodsViews);
    }

    // 사용자 행동 데이터에 따른 추천 요소 업데이트
    @PostMapping("/api/v1/recommendation/activity")
    @ResponseBody
    public ResponseEntity<Object> setRecommendationActivity(@AuthenticationPrincipal String username,
                                                            @Valid @RequestBody UserActivityElements userActivityElements) {
        if (username == null || username.equals("anonymousUser")) {
            return ResponseEntity.ok().location(URI.create("redirect:/login")).build();
        }

        userActivityElements.setUsername(username);
        if (recommendationUtils.setUserActivityRecommendation(userActivityElements)) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.internalServerError().build();
    }
}
