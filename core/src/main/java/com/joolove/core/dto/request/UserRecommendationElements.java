package com.joolove.core.dto.request;

import com.joolove.core.domain.ECategory;
import com.joolove.core.domain.EEmotion;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRecommendationElements {
    @NotNull
    private String username;
    private Short abvLimit;
    private ECategory preferredCategory;
    private EEmotion recentFeeling;

    @Builder
    public UserRecommendationElements(String username, Short abvLimit, ECategory preferredCategory, EEmotion recentFeeling) {
        this.username = username;
        this.abvLimit = abvLimit;
        this.preferredCategory = preferredCategory;
        this.recentFeeling = recentFeeling;
    }

}
