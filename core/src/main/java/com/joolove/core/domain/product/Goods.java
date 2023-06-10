package com.joolove.core.domain.product;

import com.joolove.core.domain.BaseTimeStamp;
import com.joolove.core.domain.billing.OrdersGoods;
import com.joolove.core.domain.member.Favorite;
import com.joolove.core.domain.member.FavoriteGoods;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jetbrains.annotations.Contract;

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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "category_id", unique = true)
    private Category category;

    @NotNull
    @Column(unique = true)
    private String name;        // 상품명 (Goods, GoodsDetail, GoodsStats 에서 사용한다.)

    private Integer price;

    private Integer stock;

    private String description;

    private Long salesFigures;

    @NotNull
    private Short salesStatus;      // 판매 상태 (0: 판매하지않음, 1: 판매중, 2: 기타)

    @Builder
    public Goods(UUID id, Category category, String name, Integer price, Integer stock, String description, Long salesFigures, Short salesStatus) {
        this.id = id;
        this.category = category;
        this.name = name;
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
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", description='" + description + '\'' +
                ", salesFigures=" + salesFigures +
                ", salesStatus=" + salesStatus +
                '}';
    }

    /* mappedBy */
    @OneToOne(mappedBy = "goods", cascade = CascadeType.PERSIST, optional = false)
    private GoodsStats goodsStats;

    @OneToOne(mappedBy = "goods", cascade = CascadeType.PERSIST, optional = false)
    private GoodsDetails goodsDetail;

    @OneToMany(mappedBy = "goods", cascade = CascadeType.PERSIST)
    private List<FavoriteGoods> favoriteGoodsList = new ArrayList<>();

    @OneToMany(mappedBy = "goods", cascade = CascadeType.PERSIST)
    private List<GoodsSale> goodsSales = new ArrayList<>();

    @OneToMany(mappedBy = "goods", cascade = CascadeType.PERSIST)
    private List<OrdersGoods> ordersGoodsList = new ArrayList<>();

    @OneToMany(mappedBy = "goods", cascade = CascadeType.PERSIST)
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

    public void changeSalesStatus(Short salesStatus) {
        this.salesStatus = salesStatus;
    }

    public void changeSalesFigures(Long salesFigures) {
        this.salesFigures = salesFigures;
    }

    public void increaseSalesFigures() {
        this.salesFigures++;
    }

    public void changeStock(Integer stock) {
        this.stock = stock;
    }

    public void increaseStock() {
        this.stock++;
    }

    public void changePrice(Integer price) {
        this.price = price;
    }

}
