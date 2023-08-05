package com.joolove.core.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRecommendationBaseRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String abvLimit;
    @NotNull
    private List<String> preferredCategory;

    @Builder
    public UserRecommendationBaseRequest(String username, String abvLimit, List<String> preferredCategory) {
        this.username = username;
        this.abvLimit = abvLimit;
        this.preferredCategory = preferredCategory;
    }
}
