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
import java.util.UUID;

@Entity
@Table(schema = "recommend")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RecommendationDaily extends BaseTimeStamp {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "recommendation_daily_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    private Short feeling;

    @NotNull
    private Short abv;

    @Builder
    public RecommendationDaily(UUID id, User user, Short feeling, Short abv) {
        this.id = id;
        this.user = user;
        this.feeling = feeling;
        this.abv = abv;
    }

    @Override
    public String toString() {
        return "RecommendationDaily{" +
                "id=" + id +
                ", user=" + user +
                ", feeling=" + feeling +
                ", abv=" + abv +
                '}';
    }

}
