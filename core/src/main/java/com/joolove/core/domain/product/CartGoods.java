package com.joolove.core.domain.product;

import com.joolove.core.domain.billing.Cart;
import com.joolove.core.domain.member.Favorite;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(catalog = "product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CartGoods {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "cart_goods_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "goods_id")
    private Goods goods;

    @NotNull
    private Integer count;              // 개수

    @Builder
    public CartGoods(UUID id, Cart cart, Goods goods, Integer count) {
        this.id = id;
        this.cart = cart;
        this.goods = goods;
        this.count = count;
    }

    @Override
    public String toString() {
        return "CartGoods{" +
                "id=" + id +
                ", cart=" + cart +
                ", goods=" + goods +
                ", count=" + count +
                '}';
    }

    public void updateCount(Integer count) {
        this.count = count;
    }
}
