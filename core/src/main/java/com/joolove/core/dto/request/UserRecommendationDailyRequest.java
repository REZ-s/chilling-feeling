package com.joolove.core.dto.request;

import com.joolove.core.domain.EEmotion;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRecommendationDailyRequest {

    private String username;
    private String recentFeeling;

    @Builder
    public UserRecommendationDailyRequest(String username, String recentFeeling) {
        this.username = username;
        this.recentFeeling = recentFeeling;
    }
}
