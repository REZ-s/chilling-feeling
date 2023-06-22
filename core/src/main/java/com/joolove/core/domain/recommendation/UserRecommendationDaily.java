package com.joolove.core.domain.recommendation;

import com.joolove.core.domain.BaseTimeStamp;
import com.joolove.core.domain.EEmotion;
import com.joolove.core.domain.member.User;
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
public class UserRecommendationDaily extends BaseTimeStamp {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "user_recommendation_daily_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @Enumerated(EnumType.STRING)
    private EEmotion feeling;      // 오늘의 기분

    @Builder
    public UserRecommendationDaily(UUID id, User user, EEmotion feeling) {
        this.id = id;
        this.user = user;
        this.feeling = feeling;
    }

    @Override
    public String toString() {
        return "UserRecommendationDaily{" +
                "id=" + id +
                ", feeling=" + feeling +
                '}';
    }

}
