package com.joolove.core.dto.query;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GoodsViewDetails implements IGoodsView {
    private String name;
    private String engName;
    private String type;
    private String imageUrl;
    private String label;
    private String score;
    private Integer reviewCount;
    private String degree;
    private String country;
    private String company;
    private String supplier;
    private String color;
    private String colorImageUrl;

    private String description;
    private String descriptionImageUrl;
    private String summary;
    private String opt1Value;   // aroma
    private String opt2Value;   // balance
    private String opt3Value;   // body
    private String opt4Value;   // tannin
    private String opt5Value;   // sweetness
    private String opt6Value;   // acidity
    private String opt7Value;   // soda

    @Builder
    public GoodsViewDetails(String name, String engName, String type, String imageUrl, String label, String score, Integer reviewCount, String degree, String country, String company, String supplier, String color, String colorImageUrl, String description, String descriptionImageUrl, String summary, String opt1Value, String opt2Value, String opt3Value, String opt4Value, String opt5Value, String opt6Value, String opt7Value) {
        this.name = name;
        this.engName = engName;
        this.type = type;
        this.imageUrl = imageUrl;
        this.label = label;
        this.score = score;
        this.reviewCount = reviewCount;
        this.degree = degree;
        this.country = country;
        this.company = company;
        this.supplier = supplier;
        this.color = color;
        this.colorImageUrl = colorImageUrl;
        this.description = description;
        this.descriptionImageUrl = descriptionImageUrl;
        this.summary = summary;
        this.opt1Value = opt1Value;
        this.opt2Value = opt2Value;
        this.opt3Value = opt3Value;
        this.opt4Value = opt4Value;
        this.opt5Value = opt5Value;
        this.opt6Value = opt6Value;
        this.opt7Value = opt7Value;
    }

    public static GoodsViewDetails buildEmpty() {
        return GoodsViewDetails.builder()
                .name("")
                .engName("")
                .type("")
                .imageUrl("")
                .label("")
                .score("")
                .reviewCount(0)
                .degree("")
                .country("")
                .company("")
                .supplier("")
                .color("")
                .colorImageUrl("")
                .description("")
                .descriptionImageUrl("")
                .summary("")
                .opt1Value("")
                .opt2Value("")
                .opt3Value("")
                .opt4Value("")
                .opt5Value("")
                .opt6Value("")
                .opt7Value("")
                .build();
    }
}
