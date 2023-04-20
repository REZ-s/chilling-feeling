package com.joolove.core.domain.base_item;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(schema = "base_item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Wine extends BaseAlcoholImpl {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "wine_id", columnDefinition = "BINARY(16)")
    private UUID id;

    // 와인 세부 정보
    private String aroma;           // 향 (comma 로 연결된 문자열)
    private Short body;             // 목 넘김 (1, 2, 3, 4, 5)
    private Short tannin;           // 떫기 (1, 2, 3, 4, 5)
    private Short sweetness;        // 당도 (1, 2, 3, 4, 5)
    private Short acidity;          // 산미 (1, 2, 3, 4, 5)

    @Builder
    public Wine(String name, String type, UUID id, String aroma, Short body, Short tannin, Short sweetness, Short acidity) {
        super(name, type);
        this.id = id;
        this.aroma = aroma;
        this.body = body;
        this.tannin = tannin;
        this.sweetness = sweetness;
        this.acidity = acidity;
    }

}
