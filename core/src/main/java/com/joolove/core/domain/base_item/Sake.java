package com.joolove.core.domain.base_item;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Table(schema = "base_item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Sake extends BaseAlcoholImpl {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "sake_id", columnDefinition = "BINARY(16)")
    private UUID id;

    // 사케 세부 정보
    private Short acidity;          // 산도. 담백 ~ 중후 사이  (1, 2, 3, 4, 5)
    private Short sweetness;        // 주도. 스위트 ~ 드라이 사이 (1, 2, 3, 4, 5)

    @Builder
    public Sake(String name, String engName, String type, UUID id, Short acidity, Short sweetness) {
        super(name, engName, type);
        this.id = id;
        this.acidity = acidity;
        this.sweetness = sweetness;
    }

}