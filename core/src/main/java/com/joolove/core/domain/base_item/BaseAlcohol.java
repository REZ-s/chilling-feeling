package com.joolove.core.domain.base_item;


public interface BaseAlcohol {
    String getName();       // 술 이름
    String getEngName();    // 술 영문 이름
    String getType();       // 술 종류
    String getImageUrl();   // 이미지 url
    Short getPriceLevel();  // 가격대
    String getDegree();     // 도수
    String getCountry();    // 생산국
    String getCompany();    // 생산사
    String getSupplier();   // 공급사
    String getColor();      // 색상
    String getDescription();    // 설명

}
