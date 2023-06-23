package com.joolove.core.controller;

import com.joolove.core.dto.query.UserActivityElements;
import com.joolove.core.dto.request.UserRecommendationBaseRequest;
import com.joolove.core.dto.request.UserRecommendationDailyRequest;
import com.joolove.core.utils.algorithm.RecommendationComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(originPatterns = "*", allowCredentials = "true", maxAge = 3600)
@Controller
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationComponent recommendationComponent;

    @PostMapping("/api/v1/recommendation/base")
    @ResponseBody
    public ResponseEntity<Object> setRecommendationBase(@Valid @RequestBody UserRecommendationBaseRequest userRecommendationBaseRequest) {
        if (recommendationComponent.setUserRecommendationBase(userRecommendationBaseRequest)) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.internalServerError().build();
    }

    @PostMapping("/api/v1/recommendation/daily")
    @ResponseBody
    public ResponseEntity<Object> setRecommendationDaily(@Valid @RequestBody UserRecommendationDailyRequest userRecommendationDailyRequest) {
        if (recommendationComponent.setUserRecommendationDaily(userRecommendationDailyRequest)) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.internalServerError().build();
    }

    // 사용자 행동 데이터에 따른 추천 요소 업데이트
    @PostMapping("/api/v1/recommendation/activity")
    @ResponseBody
    public ResponseEntity<Object> setRecommendationActivity(
            @Valid @RequestBody String username,
            @Valid @RequestBody UserActivityElements userActivityElements) {
        if (recommendationComponent.setUserActivityRecommendation(username, userActivityElements)) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.internalServerError().build();
    }
}
