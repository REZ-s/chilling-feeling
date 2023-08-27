package com.joolove.core.domain.product;

import com.joolove.core.domain.BaseTimeStamp;
import com.mysql.cj.util.StringUtils;
import io.netty.util.internal.StringUtil;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(catalog = "product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GoodsStats extends BaseTimeStamp {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "goods_stats_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "goods_id")
    private Goods goods;

    @NotNull
    private String label = "new";     // 라벨 (예: 신상품, 베스트, 스테디, 가성비 등)

    @NotNull
    private String score = "0.0";     // 평점 (예: 값이 4.45이면 4.5와 같이 소숫점 1자리까지만 표시. 반올림)

    @NotNull
    private Integer reviewCount = 0;  // 리뷰 숫자

    @NotNull
    private Integer heartCount = 0;   // 좋아요 (찜) 숫자

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    @Builder
    public GoodsStats(UUID id, Goods goods, String label, String score, Integer reviewCount, Integer heartCount) {
        this.id = id;
        this.goods = goods;
        this.score = score;
        this.reviewCount = reviewCount;
        this.label = label;
        this.heartCount = heartCount;
    }

    public static GoodsStats buildGoodsStatsBase(Goods goods) {
        return GoodsStats.builder()
                .goods(goods)
                .label("new")
                .score("0.0")
                .heartCount(0)
                .reviewCount(0)
                .build();
    }
}