package com.joolove.core.domain.recommend;

import com.joolove.core.domain.BaseTimeStamp;
import com.joolove.core.domain.ECategory;
import com.joolove.core.domain.member.User;
import lombok.*;
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
public class UserRecommendationBase extends BaseTimeStamp {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "user_recommendation_base_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    private Short abvLimit;     // 알콜 도수 제한 (0 ~ 100)

    @NotNull
    @Enumerated(EnumType.STRING)
    private ECategory preferredCategory;   // 선호하는 카테고리

    @Builder
    public UserRecommendationBase(UUID id, User user, Short abvLimit, ECategory preferredCategory) {
        this.id = id;
        this.user = user;
        this.abvLimit = abvLimit;
        this.preferredCategory = preferredCategory;
    }

    @Override
    public String toString() {
        return "UserRecommendationBase{" +
                "id=" + id +
                ", user=" + user +
                ", abvLimit=" + abvLimit +
                ", preferredCategory='" + preferredCategory + '\'' +
                '}';
    }

    /* mappedBy */
    @OneToMany(mappedBy = "userRecommendationBase")
    private List<RecommendationFirstInterestKeyword> recommendationFirstInterestKeywordList = new ArrayList<>();

    public void setRecommendationFirstInterestKeywordList(List<RecommendationFirstInterestKeyword> recommendationFirstInterestKeywordList) {
        this.recommendationFirstInterestKeywordList = recommendationFirstInterestKeywordList;
    }
}
