package com.joolove.core.domain.product;

import com.joolove.core.domain.BaseTimeStamp;
import com.joolove.core.domain.billing.OrdersGoods;
import com.joolove.core.domain.member.Favorite;
import com.joolove.core.domain.member.FavoriteGoods;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(schema = "product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Goods extends BaseTimeStamp {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "goods_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @NotNull
    private String goodsName;

    @NotNull
    private Integer price;

    @NotNull
    private Integer stock;

    @NotNull
    private String description;

    @NotNull
    private Long salesFigures;

    @NotNull
    private Short salesStatus;

    @Builder
    public Goods(UUID id, Category category, String goodsName, Integer price, Integer stock, String description, Long salesFigures, Short salesStatus) {
        this.id = id;
        this.category = category;
        this.goodsName = goodsName;
        this.price = price;
        this.stock = stock;
        this.description = description;
        this.salesFigures = salesFigures;
        this.salesStatus = salesStatus;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "id=" + id +
                ", category=" + category +
                ", goodsName='" + goodsName + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", description='" + description + '\'' +
                ", salesFigures=" + salesFigures +
                ", salesStatus=" + salesStatus +
                '}';
    }

    /* mappedBy */
    @OneToMany(mappedBy = "goods", cascade = CascadeType.PERSIST)
    private List<FavoriteGoods> favoriteGoodsList = new ArrayList<>();

    @OneToMany(mappedBy = "goods", cascade = CascadeType.PERSIST)
    private List<GoodsSale> goodsSales = new ArrayList<>();

    @OneToMany(mappedBy = "goods")
    private List<OrdersGoods> ordersGoodsList = new ArrayList<>();

    @OneToMany(mappedBy = "goods")
    private List<GoodsRelatedKeyword> goodsRelatedKeywords = new ArrayList<>();

    public void setFavoriteGoodsList(List<FavoriteGoods> favoriteGoodsList) {
        this.favoriteGoodsList = favoriteGoodsList;
    }

    public void setGoodsSales(List<GoodsSale> goodsSales) {
        this.goodsSales = goodsSales;
    }

    public void setOrderGoodsList(List<OrdersGoods> ordersGoodsList) {
        this.ordersGoodsList = ordersGoodsList;
    }

    public void setGoodsRelatedKeywords(List<GoodsRelatedKeyword> goodsRelatedKeywords) {
        this.goodsRelatedKeywords = goodsRelatedKeywords;
    }

}
