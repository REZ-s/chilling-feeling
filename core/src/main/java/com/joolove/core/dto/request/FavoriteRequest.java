package com.joolove.core.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoriteRequest {
    @NotBlank
    @Size(min = 4, max = 40)
    private String username;

    @NotBlank
    private String goodsName;

    @Builder
    public FavoriteRequest(String username, String goodsName) {
        this.username = username;
        this.goodsName = goodsName;
    }
}
