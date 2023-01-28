package com.joolove.core.domain.billing;

import com.joolove.core.domain.BaseTimeStamp;
import com.joolove.core.domain.product.Goods;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(schema = "billing")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrdersGoods extends BaseTimeStamp {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "orders_goods_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private Orders order;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "goods_id")
    private Goods goods;

    @NotNull
    private Integer singleSalePrice;

    @NotNull
    private Integer count;

    @Builder
    public OrdersGoods(UUID id, Orders order, Goods goods, Integer singleSalePrice, Integer count) {
        this.id = id;
        this.order = order;
        this.goods = goods;
        this.singleSalePrice = singleSalePrice;
        this.count = count;
    }

    @Override
    public String toString() {
        return "OrdersGoods{" +
                "id=" + id +
                ", order=" + order +
                ", goods=" + goods +
                ", singleSalePrice=" + singleSalePrice +
                ", count=" + count +
                '}';
    }

}