package com.joolove.core.dto.query;

import com.joolove.core.domain.ECategory;
import com.joolove.core.domain.EEmotion;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRecommendationElements {
    @NotNull
    private String username;
    private String abvLimit;
    private List<ECategory> preferredCategories;
    private EEmotion recentFeeling;

    @Builder
    public UserRecommendationElements(String username, String abvLimit, List<ECategory>  preferredCategories, EEmotion recentFeeling) {
        this.username = username;
        this.abvLimit = abvLimit;
        this.preferredCategories = preferredCategories;
        this.recentFeeling = recentFeeling;
    }
}
