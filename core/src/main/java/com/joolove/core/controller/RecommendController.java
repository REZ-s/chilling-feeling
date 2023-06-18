package com.joolove.core.controller;

import com.joolove.core.utils.algorithm.RecommendComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(originPatterns = "*", allowCredentials = "true", maxAge = 3600)
@Controller
@RequiredArgsConstructor
public class RecommendController {

    private final RecommendComponent recommendComponent;

    // 기본 추천 설정하기
    @PostMapping("/api/v1/recommendation/base")
    @ResponseBody
    public ResponseEntity<?> getAuthenticationCodeEmail(@Valid @RequestBody String email) {
        if (recommendComponent.setBaseRecommend(email)) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.internalServerError().build();
    }

}
