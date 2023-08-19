package com.joolove.core.controller;

import com.joolove.core.domain.log.UserActivityLog;
import com.joolove.core.dto.query.IGoodsView;
import com.joolove.core.dto.query.UserActivityLogElements;
import com.joolove.core.dto.request.UserRecommendationBaseRequest;
import com.joolove.core.dto.request.UserRecommendationDailyRequest;
import com.joolove.core.utils.RecommendationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;

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

    @GetMapping("/api/v1/recommendation/base/keyword")
    @ResponseBody
    public ResponseEntity<Object> getRecommendationBaseKeyword(@AuthenticationPrincipal String username) {
        if (username == null || username.equals("anonymousUser")) {
            return ResponseEntity.ok().location(URI.create("redirect:/login")).build();
        }

        String categories = recommendationUtils.getPreferredCategories(username);
        return ResponseEntity.ok().body(categories);
    }

    @GetMapping("/api/v1/recommendation/daily/feeling")
    @ResponseBody
    public ResponseEntity<Object> getRecommendationDailyFeeling(@AuthenticationPrincipal String username) {
        if (username == null || username.equals("anonymousUser")) {
            return ResponseEntity.ok().location(URI.create("redirect:/login")).build();
        }

        String feeling = recommendationUtils.getDailyFeeling(username);
        return ResponseEntity.ok().body(feeling);
    }

    // 사용자 행동 로그 적재
    @PostMapping("/api/v1/recommendation/log/activity")
    @ResponseBody
    public ResponseEntity<Object> setRecommendationActivityLog(@Valid @RequestBody UserActivityLogElements userActivityLogElements) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.badRequest().build();
        }

        String username = (String) authentication.getPrincipal();
        userActivityLogElements.setUsername(username);

        if (recommendationUtils.setUserActivityRecommendation(userActivityLogElements)) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.internalServerError().build();
    }

}
