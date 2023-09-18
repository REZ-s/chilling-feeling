package com.joolove.core.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrdersRequest {
    @NotBlank
    @Size(min = 4, max = 40)
    private String username;

    @NotNull
    private List<String> goodsNameList;

    @NotNull
    private List<Integer> goodsCountList;

    @Builder
    public OrdersRequest(String username, List<String> goodsNameList, List<Integer> goodsCountList) {
        this.username = username;
        this.goodsNameList = goodsNameList;
        this.goodsCountList = goodsCountList;
    }

}
