package com.joolove.core.controller;

import com.joolove.core.dto.query.IGoodsView;
import com.joolove.core.dto.query.UserActivityElements;
import com.joolove.core.dto.request.UserRecommendationBaseRequest;
import com.joolove.core.dto.request.UserRecommendationDailyRequest;
import com.joolove.core.utils.RecommendationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(originPatterns = "*", allowCredentials = "true", maxAge = 3600)
@Controller
@RequiredArgsConstructor
public class RecommendationController {
    private final RecommendationUtils recommendationUtils;

    @PostMapping("/api/v1/recommendation/base")
    @ResponseBody
    public ResponseEntity<Object> setRecommendationBase(@AuthenticationPrincipal String username,
                                                        @Valid @RequestBody UserRecommendationBaseRequest userRecommendationBaseRequest) {
        userRecommendationBaseRequest.setUsername(username);

        if (recommendationUtils.setUserRecommendationBase(userRecommendationBaseRequest)) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.internalServerError().build();
    }

    @PostMapping("/api/v1/recommendation/daily")
    @ResponseBody
    public ResponseEntity<Object> setRecommendationDaily(@AuthenticationPrincipal String username,
                                                         @Valid @RequestBody UserRecommendationDailyRequest userRecommendationDailyRequest) {
        userRecommendationDailyRequest.setUsername(username);

        if (recommendationUtils.setUserRecommendationDaily(userRecommendationDailyRequest)) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.internalServerError().build();
    }

    @GetMapping("/api/v1/recommendation/daily")
    @ResponseBody
    public ResponseEntity<Object> getRecommendationDaily(@AuthenticationPrincipal String username) {
        List<IGoodsView> goodsViews = recommendationUtils.getUserRecommendationGoodsList(username);
        return ResponseEntity.ok().body(goodsViews);
    }

    @GetMapping("/api/v1/recommendation")
    @ResponseBody
    public ResponseEntity<Object> getRecommendation(@AuthenticationPrincipal String username) {
        List<IGoodsView> goodsViews = recommendationUtils.getUserRecommendationGoodsList(username);
        return ResponseEntity.ok().body(goodsViews);
    }

    // 사용자 행동 데이터에 따른 추천 요소 업데이트
    @PostMapping("/api/v1/recommendation/activity")
    @ResponseBody
    public ResponseEntity<Object> setRecommendationActivity(@AuthenticationPrincipal String username,
                                                            @Valid @RequestBody UserActivityElements userActivityElements) {
        userActivityElements.setUsername(username);

        if (recommendationUtils.setUserActivityRecommendation(userActivityElements)) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.internalServerError().build();
    }
}
