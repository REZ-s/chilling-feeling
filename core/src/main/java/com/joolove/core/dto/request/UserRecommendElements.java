package com.joolove.core.dto.request;

import com.joolove.core.domain.ECategory;
import com.joolove.core.domain.EEmotion;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRecommendElements {
    private Short abvLimit;
    private ECategory preferredCategory;
    private EEmotion recentFeeling;

    @Builder
    public UserRecommendElements(Short abvLimit, ECategory preferredCategory, EEmotion recentFeeling) {
        this.abvLimit = abvLimit;
        this.preferredCategory = preferredCategory;
        this.recentFeeling = recentFeeling;
    }

    public static UserRecommendElements buildEmpty() {
        return UserRecommendElements.builder()
                .abvLimit((short) 100)
                .preferredCategory(ECategory.All)
                .recentFeeling(EEmotion.BLANK)
                .build();
    }
}
