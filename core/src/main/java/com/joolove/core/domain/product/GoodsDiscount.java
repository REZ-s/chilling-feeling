package com.joolove.core.domain.product;

import com.joolove.core.domain.BaseTimeStamp;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(catalog = "product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GoodsDiscount extends BaseTimeStamp {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "goods_sale_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "goods_id")
    private Goods goods;

    @NotNull
    private Short discountRate;  // 할인율

    @NotNull
    private Short saleType;     // 할인 종류 (0: 없음, 1: 일반할인)

    private String description;

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    @Builder
    public GoodsDiscount(UUID id, Goods goods, Short discountRate, Short saleType, String description) {
        this.id = id;
        this.goods = goods;
        this.discountRate = discountRate;
        this.saleType = saleType;
        this.description = description;
    }

}
