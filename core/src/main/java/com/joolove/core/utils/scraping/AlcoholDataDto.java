package com.joolove.core.utils.scraping;

import lombok.*;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlcoholDataDto {
    private String name;
    private String type;

    @Builder
    public AlcoholDataDto(String name, String type) {
        this.name = name;
        this.type = type;
    }
}
