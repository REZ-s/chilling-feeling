package com.joolove.core.domain.member;

import com.joolove.core.domain.BaseTimeStamp;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(schema = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Profile extends BaseTimeStamp {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "profile_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotBlank
    @Column(unique = true)
    private String nickname;

    private String imageUrl;

    private String introduction;

    @Builder
    public Profile(UUID id, User user, String nickname, String imageUrl, String introduction) {
        this.id = id;
        this.user = user;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.introduction = introduction;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "id=" + id +
                ", user=" + user +
                ", nickname='" + nickname + '\'' +
                ", imageUrl=" + imageUrl +
                ", introduction='" + introduction + '\'' +
                '}';
    }

}