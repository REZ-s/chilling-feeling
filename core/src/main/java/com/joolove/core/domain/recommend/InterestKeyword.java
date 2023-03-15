package com.joolove.core.domain.recommend;

import com.joolove.core.domain.BaseTimeStamp;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.jmx.export.annotation.ManagedNotifications;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(schema = "recommend")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class InterestKeyword extends BaseTimeStamp {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "interest_keyword_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @NotNull
    private String interestKeywordName;

    @Builder
    public InterestKeyword(UUID id, String interestKeywordName) {
        this.id = id;
        this.interestKeywordName = interestKeywordName;
    }

    @Override
    public String toString() {
        return "InterestKeyword{" +
                "id=" + id +
                ", interestKeywordName='" + interestKeywordName + '\'' +
                '}';
    }

    /* mappedBy */
    @OneToMany(mappedBy = "interestKeyword")
    private List<RecommendationFirstInterestKeyword> recommendationFirstInterestKeywordList = new ArrayList<>();

    public void setRecommendationFirstInterestKeywordList(List<RecommendationFirstInterestKeyword> recommendationFirstInterestKeywordList) {
        this.recommendationFirstInterestKeywordList = recommendationFirstInterestKeywordList;
    }
}