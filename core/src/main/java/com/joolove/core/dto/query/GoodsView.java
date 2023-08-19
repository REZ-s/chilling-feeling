package com.joolove.core.dto.query;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GoodsView implements IGoodsView {
    private String name;
    private String type;
    private String subType;
    private String imageUrl;
    private String label;
    private String score;
    private Integer reviewCount;

    @Builder
    public GoodsView(String name, String type, String subType, String imageUrl, String label, String score, Integer reviewCount) {
        this.name = name;
        this.type = type;
        this.subType = subType;
        this.imageUrl = imageUrl;
        this.label = label;
        this.score = score;
        this.reviewCount = reviewCount;
    }

    public static GoodsView buildEmpty() {
        return GoodsView.builder()
                .name("")
                .type("")
                .subType("")
                .imageUrl("")
                .label("")
                .score("")
                .reviewCount(0)
                .build();
    }
}