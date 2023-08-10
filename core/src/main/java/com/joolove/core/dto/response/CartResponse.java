package com.joolove.core.dto.response;

import com.joolove.core.dto.query.GoodsView;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartResponse {
    @NotBlank
    private GoodsView goodsView;

    @NotNull
    private Integer goodsCount;

    @Builder
    public CartResponse(GoodsView goodsView, Integer goodsCount) {
        this.goodsView = goodsView;
        this.goodsCount = goodsCount;
    }
}
