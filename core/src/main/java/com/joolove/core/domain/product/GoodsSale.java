package com.joolove.core.domain.product;

import com.joolove.core.domain.BaseTimeStamp;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(schema = "product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GoodsSale extends BaseTimeStamp {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "goods_sale_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_id")
    private Goods goods;

    @NotNull
    private String description;

    @NotNull
    private Long discountRate;

    @NotNull
    private Short saleType;

    @Builder
    public GoodsSale(UUID id, Goods goods, String description, Long discountRate, Short saleType) {
        this.id = id;
        this.goods = goods;
        this.description = description;
        this.discountRate = discountRate;
        this.saleType = saleType;
    }

    @Override
    public String toString() {
        return "GoodsSale{" +
                "id=" + id +
                ", goods=" + goods +
                ", description='" + description + '\'' +
                ", discountRate=" + discountRate +
                ", saleType=" + saleType +
                '}';
    }

}
