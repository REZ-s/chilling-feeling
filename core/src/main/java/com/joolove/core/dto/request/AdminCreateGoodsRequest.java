package com.joolove.core.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminCreateGoodsRequest {
    private String name;
    private Short salesStatus;
    private String type;
    private String categoryName;
    private String goodsName;
    private String engName;
    private String imageUrl;
    private String color;
    private String colorImageUrl;
    private String descriptionImageUrl;
    private String description;
    private String summary;
    private String country;
    private String company;
    private String supplier;
    private String degree;
    private String aroma;
    private String balance;
    private String body;
    private String tannin;
    private String acidity;
    private String sweetness;
    private String soda;
    private String label;
    private String score;
    private Integer reviewCount;
}
