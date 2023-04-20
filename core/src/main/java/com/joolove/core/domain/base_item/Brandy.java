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
public class Brandy extends BaseAlcoholImpl {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "brandy_id", columnDefinition = "BINARY(16)")
    private UUID id;

    // 브랜디 세부 정보
    private String aroma;           // 향 (comma 로 연결된 문자열)

    @Builder
    public Brandy(String name, String type, UUID id, String aroma) {
        super(name, type);
        this.id = id;
        this.aroma = aroma;
    }
}
