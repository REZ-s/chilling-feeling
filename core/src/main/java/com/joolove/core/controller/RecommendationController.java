package com.joolove.core.controller;

import com.joolove.core.dto.request.UserActivityElements;
import com.joolove.core.dto.request.UserRecommendElements;
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

    // 사용자가 직접 추천 설정하기
    @PostMapping("/api/v1/recommendation")
    @ResponseBody
    public ResponseEntity<Object> setRecommendation(
            @Valid @RequestBody String username,
            @Valid @RequestBody UserRecommendElements userRecommendElements) {
        if (recommendationComponent.setUserRecommendation(username, userRecommendElements)) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.internalServerError().build();
    }

    // 사용자 행동 데이터에 따른 추천 요소 업데이트
    @PostMapping("/api/v1/recommendation/activity")
    @ResponseBody
    public ResponseEntity<Object> updateRecommendation(
            @Valid @RequestBody String username,
            @Valid @RequestBody UserActivityElements userActivityElements) {
        if (recommendationComponent.setUserActivityRecommendation(username, userActivityElements)) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.internalServerError().build();
    }
}
