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
public class Spirits extends BaseAlcoholImpl {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "spirits_id", columnDefinition = "BINARY(16)")
    private UUID id;

    // 일반 증류주 세부 정보
    private String aroma;           // 향 (comma 로 연결된 문자열)
    private Short soda;             // 탄산 정도. 높으면 탄산이 쎄다. (1, 2, 3, 4, 5)
    private Short balance;          // 알코올과 혼합성분과의 균형. 높으면 알코올향이 쎄다. (1, 2, 3, 4, 5)

    @Builder
    public Spirits(String name, String type, UUID id, String aroma, Short soda, Short balance) {
        super(name, type);
        this.id = id;
        this.aroma = aroma;
        this.soda = soda;
        this.balance = balance;
    }
}
