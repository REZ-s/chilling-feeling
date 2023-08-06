package com.joolove.core.domain.recommendation;

import com.joolove.core.domain.BaseTimeStamp;
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
@Table(catalog = "recommendation")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserRecommendationDaily extends BaseTimeStamp {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "user_recommendation_daily_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
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

    public void setFeeling(EEmotion feeling) {
        this.feeling = feeling;
    }

    @Getter
    public enum EEmotion {
        SMILE("즐거워요"),  // "즐거워요"
        HAPPY("기뻐요"),  // "기뻐요"
        SAD("슬퍼요"),    // "슬퍼요"
        ANGRY("화나요"),  // "화나요"
        BLANK("모르겠어요");   // 그 외

        private String displayName;

        EEmotion(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
