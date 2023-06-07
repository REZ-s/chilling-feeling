package com.joolove.core.domain.product;

import com.joolove.core.domain.BaseTimeStamp;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(schema = "product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class GoodsStats extends BaseTimeStamp {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "goods_stats_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @OneToOne(mappedBy = "goodsStats")
    private Goods goods;

    private String label;
    private String score;
    private Integer reviewCount;

    @Builder
    public GoodsStats(UUID id, Goods goods, String label, String score, Integer reviewCount) {
        this.id = id;
        this.goods = goods;
        this.score = score;
        this.reviewCount = reviewCount;
        this.label = label;
    }


}