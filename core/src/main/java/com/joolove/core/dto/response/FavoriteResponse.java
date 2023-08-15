package com.joolove.core.dto.response;

import com.joolove.core.dto.query.GoodsView;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoriteResponse {
    @NotBlank
    private GoodsView goodsView;

    @Builder
    public FavoriteResponse(GoodsView goodsView) {
        this.goodsView = goodsView;
    }
}