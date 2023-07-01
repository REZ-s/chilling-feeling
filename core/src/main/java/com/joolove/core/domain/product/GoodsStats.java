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
@Table(schema = "product")
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
    @JoinColumn(name = "goods_id", unique = true)
    private Goods goods;

    private String label = StringUtil.EMPTY_STRING;     // 라벨 (예: 신상품, 베스트, 스테디, 가성비 등)
    private String score = StringUtil.EMPTY_STRING;     // 평점 (예: 값이 4.45이면 4.5와 같이 소숫점 1자리까지만 표시. 반올림 반영)
    private Integer reviewCount = 0;                    // 리뷰 숫자
    private Integer heartCount = 0;                     // 좋아요 (찜) 숫자

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


}