package com.joolove.core.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrdersRequest {
    @NotBlank
    @Size(min = 4, max = 40)
    private String username;

    @NotBlank
    private String goodsName;

    @NotNull
    private Short goodsCount;

    @Builder
    public OrdersRequest(String username, String goodsName, Short goodsCount) {
        this.username = username;
        this.goodsName = goodsName;
        this.goodsCount = goodsCount;
    }

}
