package com.joolove.core.domain.product;

import com.joolove.core.domain.BaseTimeStamp;
import com.mysql.cj.util.StringUtils;
import io.netty.util.internal.StringUtil;
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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, optional = false)
    @JoinColumn(name = "goods_id", nullable = false, unique = true)
    private Goods goods;

    private String label = StringUtil.EMPTY_STRING;
    private String score = StringUtil.EMPTY_STRING;
    private Integer reviewCount = 0;

    @Builder
    public GoodsStats(UUID id, Goods goods, String label, String score, Integer reviewCount) {
        this.id = id;
        this.goods = goods;
        this.score = score;
        this.reviewCount = reviewCount;
        this.label = label;
    }


}