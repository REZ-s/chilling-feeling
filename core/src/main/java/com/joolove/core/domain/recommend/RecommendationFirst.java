package com.joolove.core.domain.recommend;

import com.joolove.core.domain.BaseTimeStamp;
import com.joolove.core.domain.member.User;
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
@Table(schema = "recommend")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RecommendationFirst extends BaseTimeStamp {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "recommendation_first_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    private Short drinkCapacity;

    @NotNull
    private Integer priceLimit;

    @Builder
    public RecommendationFirst(UUID id, User user, Short drinkCapacity, Integer priceLimit) {
        this.id = id;
        this.user = user;
        this.drinkCapacity = drinkCapacity;
        this.priceLimit = priceLimit;
    }

    @Override
    public String toString() {
        return "RecommendationFirst{" +
                "id=" + id +
                ", user=" + user +
                ", drinkCapacity=" + drinkCapacity +
                ", priceLimit=" + priceLimit +
                '}';
    }

    /* mappedBy */
    @OneToMany(mappedBy = "recommendationFirst")
    private List<RecommendationFirstInterestKeyword> recommendationFirstInterestKeywordList = new ArrayList<>();

    public void setRecommendationFirstInterestKeywordList(List<RecommendationFirstInterestKeyword> recommendationFirstInterestKeywordList) {
        this.recommendationFirstInterestKeywordList = recommendationFirstInterestKeywordList;
    }
}
