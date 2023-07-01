package com.joolove.core.domain.recommendation;

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
@Table(schema = "recommendation")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserRecommendationBase extends BaseTimeStamp {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "user_recommendation_base_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @NotNull
    private String abvLimit;     // 알콜 도수 제한 (0 ~ 100)

    @NotNull
    private String preferredCategories;   // 선호하는 카테고리. List<ECategory> -> List<String> -> String

    @Builder
    public UserRecommendationBase(UUID id, User user, String abvLimit, String preferredCategories) {
        this.id = id;
        this.user = user;
        this.abvLimit = abvLimit;
        this.preferredCategories = preferredCategories;
    }

    @Override
    public String toString() {
        return "UserRecommendationBase{" +
                "id=" + id +
                ", user=" + user +
                ", abvLimit=" + abvLimit +
                ", preferredCategories='" + preferredCategories + '\'' +
                '}';
    }

    /* mappedBy */
    @OneToMany(mappedBy = "userRecommendationBase", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<RecommendationFirstInterestKeyword> recommendationFirstInterestKeywordList = new ArrayList<>();

    public void setRecommendationFirstInterestKeywordList(List<RecommendationFirstInterestKeyword> recommendationFirstInterestKeywordList) {
        this.recommendationFirstInterestKeywordList = recommendationFirstInterestKeywordList;
    }

    public void setAbvLimit(String abvLimit) {
        this.abvLimit = abvLimit;
    }

    public void setPreferredCategories(String preferredCategories) {
        this.preferredCategories = preferredCategories;
    }
}
