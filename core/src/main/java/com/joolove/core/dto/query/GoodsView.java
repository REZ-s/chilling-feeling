package com.joolove.core.dto.query;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GoodsView {
    private String name;
    private String type;
    private String imageUrl;
    private String label;
    private String score;
    private Integer reviewCount;

    @Builder
    public GoodsView(String name, String type, String imageUrl, String label, String score, Integer reviewCount) {
        this.name = name;
        this.type = type;
        this.imageUrl = imageUrl;
        this.label = label;
        this.score = score;
        this.reviewCount = reviewCount;
    }

    public static GoodsView buildEmpty() {
        return GoodsView.builder()
                .name("")
                .type("")
                .imageUrl("")
                .label("")
                .score("")
                .reviewCount(0)
                .build();
    }
}