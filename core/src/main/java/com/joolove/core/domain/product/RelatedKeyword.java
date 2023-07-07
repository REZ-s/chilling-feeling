package com.joolove.core.domain.product;

import com.joolove.core.domain.BaseTimeStamp;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(catalog = "product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RelatedKeyword extends BaseTimeStamp {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "related_keyword_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @NotNull
    private String relatedKeywordName;

    @Builder
    public RelatedKeyword(UUID id, String relatedKeywordName) {
        this.id = id;
        this.relatedKeywordName = relatedKeywordName;
    }

    @Override
    public String toString() {
        return "RelatedKeyword{" +
                "id=" + id +
                ", relatedKeywordName='" + relatedKeywordName + '\'' +
                '}';
    }

    /* mappedBy */
    @OneToMany(mappedBy = "relatedKeyword")
    private List<GoodsRelatedKeyword> goodsRelatedKeywordList = new ArrayList<>();

    public void setGoodsRelatedKeywordList(List<GoodsRelatedKeyword> goodsRelatedKeywordList) {
        this.goodsRelatedKeywordList = goodsRelatedKeywordList;
    }
}