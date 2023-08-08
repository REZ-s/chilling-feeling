package com.joolove.core.dto.query;

import com.joolove.core.domain.product.Category;
import com.joolove.core.domain.recommendation.UserRecommendationDaily;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRecommendationElements {
    @NotNull
    private String username;
    private String abvLimit;
    @Enumerated(EnumType.STRING)
    private List<Category.ECategory> preferredCategories;
    @Enumerated(EnumType.STRING)
    private UserRecommendationDaily.EEmotion recentFeeling;

    @Builder
    public UserRecommendationElements(String username, String abvLimit, List<Category.ECategory>  preferredCategories, UserRecommendationDaily.EEmotion recentFeeling) {
        this.username = username;
        this.abvLimit = abvLimit;
        this.preferredCategories = preferredCategories;
        this.recentFeeling = recentFeeling;
    }
}
