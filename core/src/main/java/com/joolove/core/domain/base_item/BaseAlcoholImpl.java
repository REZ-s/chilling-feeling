package com.joolove.core.domain.base_item;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BaseAlcoholImpl implements BaseAlcohol {

    @NotBlank
    private String name;
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

    @Builder
    public BaseAlcoholImpl(String name, String type) {
        this.name = name;
        this.type = type;
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
