package com.joolove.core.domain.base_item;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BaseAlcoholImpl implements BaseAlcohol {

    @NotBlank
    private String name;
    @NotBlank
    private String engName;
    @NotBlank
    private String type;

    protected String imageUrl;
    protected Short priceLevel;
    protected String degree;
    protected String country;
    protected String company;
    protected String supplier;
    protected String color;
    protected String description;

    @Builder(builderClassName = "MinBuilder", builderMethodName = "minBuilder")
    public BaseAlcoholImpl(String name, String engName, String type) {
        this.name = name;
        this.engName = engName;
        this.type = type;
    }

    @Builder(builderClassName = "FullBuilder", builderMethodName = "fullBuilder")
    public BaseAlcoholImpl(String name, String engName, String type, String imageUrl, Short priceLevel, String degree, String country, String company, String supplier, String color, String description) {
        this.name = name;
        this.engName = engName;
        this.type = type;
        this.imageUrl = imageUrl;
        this.priceLevel = priceLevel;
        this.degree = degree;
        this.country = country;
        this.company = company;
        this.supplier = supplier;
        this.color = color;
        this.description = description;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getEngName() {
        return engName;
    }

    @Override
    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public Short getPriceLevel() {
        return priceLevel;
    }

    @Override
    public String getDegree() {
        return degree;
    }

    @Override
    public String getCountry() {
        return country;
    }

    @Override
    public String getCompany() {
        return company;
    }

    @Override
    public String getSupplier() {
        return supplier;
    }

    @Override
    public String getColor() {
        return color;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
