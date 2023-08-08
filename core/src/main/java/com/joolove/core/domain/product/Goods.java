package com.joolove.core.domain.product;

import com.joolove.core.domain.BaseTimeStamp;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(catalog = "product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Goods extends BaseTimeStamp {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "goods_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @NotBlank
    @Column(unique = true)
    private String name;            // 상품명 (GoodsDetails 테이블의 name 과 동일)

    @NotBlank
    private String categoryName;

    @NotNull
    private Short salesStatus;      // 판매 상태 (0: 준비중, 1: 판매중, 2: 판매중지)

    @NotNull
    private Integer price;

    @NotNull
    private Integer stock;          // 재고

    @NotNull
    private Long salesFigures;      // 판매량

    private String description;

    @Builder
    public Goods(UUID id, String name, String categoryName, Integer price, Integer stock, Long salesFigures, Short salesStatus, String description) {
        this.id = id;
        this.name = name;
        this.categoryName = categoryName;
        this.price = price;
        this.stock = stock;
        this.description = description;
        this.salesFigures = salesFigures;
        this.salesStatus = salesStatus;
    }

    /* mappedBy */
    @OneToOne(mappedBy = "goods", cascade = CascadeType.PERSIST, optional = false)
    private GoodsStats goodsStats;

    @OneToOne(mappedBy = "goods", cascade = CascadeType.PERSIST, optional = false)
    private GoodsDetails goodsDetail;

    @OneToMany(mappedBy = "goods", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<FavoriteGoods> favoriteGoodsList = new ArrayList<>();

    @OneToMany(mappedBy = "goods", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<CartGoods> cartGoodsList = new ArrayList<>();

    @OneToMany(mappedBy = "goods", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<OrdersGoods> ordersGoodsList = new ArrayList<>();

    @OneToMany(mappedBy = "goods", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<GoodsDiscount> goodsDiscounts = new ArrayList<>();

    @OneToMany(mappedBy = "goods", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<GoodsRelatedKeyword> goodsRelatedKeywords = new ArrayList<>();

    public void setFavoriteGoodsList(List<FavoriteGoods> favoriteGoodsList) {
        this.favoriteGoodsList = favoriteGoodsList;
    }

    public void setCartGoodsList(List<CartGoods> cartGoodsList) {
        this.cartGoodsList = cartGoodsList;
    }

    public void setOrdersGoodsList(List<OrdersGoods> ordersGoodsList) {
        this.ordersGoodsList = ordersGoodsList;
    }

    public void setGoodsDiscounts(List<GoodsDiscount> goodsDiscounts) {
        this.goodsDiscounts = goodsDiscounts;
    }

    public void setGoodsRelatedKeywords(List<GoodsRelatedKeyword> goodsRelatedKeywords) {
        this.goodsRelatedKeywords = goodsRelatedKeywords;
    }

    public void updateSalesStatus(Short salesStatus) {
        this.salesStatus = salesStatus;
    }

    public void updateSalesFigures(Long salesFigures) {
        this.salesFigures = salesFigures;
    }

    public void increaseSalesFigures() {
        this.salesFigures++;
    }

    public void updateStock(Integer stock) {
        this.stock = stock;
    }

    public void increaseStock() {
        this.stock++;
    }

    public void updatePrice(Integer price) {
        this.price = price;
    }

}
