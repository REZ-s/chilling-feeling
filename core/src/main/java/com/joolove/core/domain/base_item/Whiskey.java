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
public class Whiskey extends BaseAlcoholImpl {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "whiskey_id", columnDefinition = "BINARY(16)")
    private UUID id;

    // 위스키 세부 정보
    private String aroma;           // 향 (comma 로 연결된 문자열)
    private Short balance;          // 알코올과 곡류의 균형. 높으면 알코올향이 쎄다. (1, 2, 3, 4, 5)

    @Builder
    public Whiskey(String name, String engName, String type, UUID id, String aroma, Short balance) {
        super(name, engName, type);
        this.id = id;
        this.aroma = aroma;
        this.balance = balance;
    }

}
