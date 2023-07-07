package com.joolove.core.domain.product;

import com.joolove.core.domain.BaseTimeStamp;
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
public class GoodsRelatedKeyword extends BaseTimeStamp {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "goods_related_keyword_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "goods_id")
    private Goods goods;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "related_keyword_id")
    private RelatedKeyword relatedKeyword;

    @Builder
    public GoodsRelatedKeyword(UUID id, Goods goods, RelatedKeyword relatedKeyword) {
        this.id = id;
        this.goods = goods;
        this.relatedKeyword = relatedKeyword;
    }

    @Override
    public String toString() {
        return "GoodsRelatedKeyword{" +
                "id=" + id +
                ", goods=" + goods +
                ", relatedKeyword=" + relatedKeyword +
                '}';
    }

}
