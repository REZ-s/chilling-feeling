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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "goods_details_id")
    private GoodsDetails goodsDetails;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "goods_stats_id")
    private GoodsStats goodsStats;

    @NotNull
    private String name;

    //@NotNull
    private Integer price;

    //@NotNull
    private Integer stock;

    //@NotNull
    private String description;

    //@NotNull
    private Long salesFigures;

    @NotNull
    private Short salesStatus;

    @Builder
    public Goods(UUID id, Category category, GoodsDetails goodsDetails, GoodsStats goodsStats, String name, Integer price, Integer stock, String description, Long salesFigures, Short salesStatus) {
        this.id = id;
        this.category = category;
        this.goodsDetails = goodsDetails;
        this.goodsStats = goodsStats;
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
                ", goodsDetails=" + goodsDetails +
                ", goodsStats=" + goodsStats +
                ", name='" + name + '\'' +
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

    /**
     * DTO
     */
    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SearchRequest {
        private String name;
        private Integer price;
        private Integer stock;
        private String description;
        private Long salesFigures;
        private Short salesStatus;

        @Builder
        public SearchRequest(String name, Integer price, Integer stock, String description, Long salesFigures, Short salesStatus) {
            this.name = name;
            this.price = price;
            this.stock = stock;
            this.description = description;
            this.salesFigures = salesFigures;
            this.salesStatus = salesStatus;
        }

        public static SearchRequest buildEmpty() {
            return SearchRequest.builder()
                    .name("")
                    .price(0)
                    .stock(0)
                    .description("")
                    .salesFigures(0L)
                    .salesStatus((short) 0)
                    .build();
        }
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class GoodsView {
        private String name;
        private String type;
        private String imageUrl;
        private String label;
        private String score;
        private Integer reviewCount;

        @Builder
        public GoodsView(String name, String type, String imageUrl, String label, String score, Integer reviewCount) {
            this.name = name;
            this.type = type;
            this.imageUrl = imageUrl;
            this.label = label;
            this.score = score;
            this.reviewCount = reviewCount;
        }

        public static GoodsView buildEmpty() {
            return GoodsView.builder()
                    .name("")
                    .type("")
                    .imageUrl("")
                    .label("")
                    .score("")
                    .reviewCount(0)
                    .build();
        }
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class GoodsViewDetail {


    }
}
