package com.joolove.core.utils.scraping;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlcoholDataDto {
    // 기본 정보
    private String name;
    private String type;
    private String imageUrl;
    private Short priceLevel;

    // 상품 정보
    private String degree;
    private String country;
    private String company;
    private String supplier;

    // 상품 특징
    private String color;   // 색상
    private List<String> aroma; // 향
    private Palette palette;   // 맛 (바디, 타닌, 당도, 산미)

    // 상품 설명
    private String description;

}
