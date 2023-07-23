package com.joolove.core.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRecommendationDailyRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String recentFeeling;

    @Builder
    public UserRecommendationDailyRequest(String username, String recentFeeling) {
        this.username = username;
        this.recentFeeling = recentFeeling;
    }
}
