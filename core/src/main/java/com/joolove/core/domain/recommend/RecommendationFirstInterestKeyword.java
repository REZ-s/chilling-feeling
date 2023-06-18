package com.joolove.core.domain.recommend;

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
@Table(schema = "recommend")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RecommendationFirstInterestKeyword extends BaseTimeStamp {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "recommendation_first_interest_keyword_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_recommendation_base_id")
    private UserRecommendationBase userRecommendationBase;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "interest_keyword_id")
    private InterestKeyword interestKeyword;

    @Builder
    public RecommendationFirstInterestKeyword(UUID id, UserRecommendationBase userRecommendationBase, InterestKeyword interestKeyword) {
        this.id = id;
        this.userRecommendationBase = userRecommendationBase;
        this.interestKeyword = interestKeyword;
    }

    @Override
    public String toString() {
        return "RecommendationFirstInterestKeyword{" +
                "id=" + id +
                ", userRecommendationBase=" + userRecommendationBase +
                ", interestKeyword=" + interestKeyword +
                '}';
    }

}
