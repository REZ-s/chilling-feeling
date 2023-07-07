package com.joolove.core.domain.product;

import com.joolove.core.domain.BaseTimeStamp;
import com.joolove.core.domain.member.Favorite;
import com.joolove.core.domain.product.Goods;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(schema = "product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FavoriteGoods extends BaseTimeStamp {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "favorite_goods_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "favorite_id")
    private Favorite favorite;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "goods_id")
    private Goods goods;

    @Builder
    public FavoriteGoods(UUID id, Favorite favorite, Goods goods) {
        this.id = id;
        this.favorite = favorite;
        this.goods = goods;
    }

    @Override
    public String toString() {
        return "FavoriteGoods{" +
                "id=" + id +
                ", favorite=" + favorite +
                ", goods=" + goods +
                '}';
    }

}