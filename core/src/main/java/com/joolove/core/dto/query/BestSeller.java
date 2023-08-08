package com.joolove.core.dto.query;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BestSeller {
    private UUID goodsId;
    private Long salesCount;

    @Builder
    public BestSeller(UUID goodsId, Long salesCount) {
        this.goodsId = goodsId;
        this.salesCount = salesCount;
    }
}